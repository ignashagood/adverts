package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Lens
import org.http4k.lens.LensFailure
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.int
import org.http4k.lens.string
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.FetchSpecialistsOperation
import ru.ac.uniyar.domain.operations.GetPagesOfSpecialistsOperation
import ru.ac.uniyar.web.models.GroupOfSpecialistsVM
import ru.ac.uniyar.web.models.Paginator
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun <IN : Any, OUT>lensOrDefault(lens: Lens<IN, OUT?>, value: IN, default: OUT): OUT =
    try {
        lens.invoke(value) ?: default
    } catch (_: LensFailure) {
        default
    }

fun <IN : Any, OUT>lensOrNull(lens: Lens<IN, OUT?>, value: IN): OUT? =
    try {
        lens.invoke(value)
    } catch (_: LensFailure) {
        null
    }

val pageNumber = Query.int().defaulted("page", 1)

fun pageNumberLens(request: Request) = lensOrDefault(
    pageNumber,
    request,
    1
)

val initials = Query.string().defaulted("initials", "")

fun searchInitialsLens(request: Request) = lensOrDefault(
    initials,
    request,
    ""
)

val phoneNumber = Query.string().defaulted("phoneNumber", "")

fun searchTelephoneLens(request: Request) = lensOrDefault(
    phoneNumber,
    request,
    ""
)

fun showSpecialistsList(
    htmlViewModel: ContextAwareViewRender,
    getPagesOfSpecialistsOperation: GetPagesOfSpecialistsOperation,
    fetchSpecialistsOperation: FetchSpecialistsOperation,
    permissionsLens: RequestContextLens<RoleRules>
): HttpHandler = handler@{
    val permissions = permissionsLens(it)
    if (!permissions.agreement)
        return@handler Response(Status.UNAUTHORIZED)
    val currentPage = pageNumberLens(it)
    val searchInitial = searchInitialsLens(it)
    val searchTelephone = searchTelephoneLens(it)
    val amountOfPages =
        getPagesOfSpecialistsOperation.getPagesAmount(searchInitial, searchTelephone)
    val listOfSpecialists =
        fetchSpecialistsOperation.fetchListWithFilters(currentPage, searchInitial, searchTelephone)
    Response(Status.OK).with(
        htmlViewModel(it) of
            GroupOfSpecialistsVM(
                listOfSpecialists,
                searchInitial,
                searchTelephone,
                Paginator(it.uri, currentPage, amountOfPages)
            )
    )
}
