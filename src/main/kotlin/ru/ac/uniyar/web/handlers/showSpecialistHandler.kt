package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.http4k.routing.path
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.*
import ru.ac.uniyar.web.models.Paginator
import ru.ac.uniyar.web.models.SpecialistVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showSpecialist(
    htmlViewModel: ContextAwareViewRender,
    fetchSpecialistOperation: FetchSpecialistOperation,
    getPagesOfAdvertsOperation: GetPagesOfAdvertsOperation,
    fetchAdvertsListOperation: FetchAdvertsListOperation,
    fetchCityOperation: FetchCityOperation,
    permissionsLens: RequestContextLens<RoleRules>,
): HttpHandler = handler@{ request ->
    val permissions = permissionsLens(request)
    if (!permissions.applicationsList)
        return@handler Response(Status.UNAUTHORIZED)

    request
        .path("number")
        ?.toIntOrNull()
        ?.let {
            fetchSpecialistOperation.fetch(it)
        }
        ?.let {
            val currentPage = pageNumberLens(request)
            val cityName = fetchCityOperation.fetch(it.city)?.cityName!!
            val advertsList =
                fetchAdvertsListOperation.fetchListWithFilters(
                    currentPage, it.initials, "", ""
                )
            Response(Status.OK).with(
                htmlViewModel(request) of
                    SpecialistVM(
                        it,
                        advertsList,
                        Paginator(
                            request.uri,
                            currentPage,
                            getPagesOfAdvertsOperation.getPagesAmount(it.initials, "", "")
                        ),
                        cityName
                    )
            )
        }
        ?: Response(Status.BAD_REQUEST)
}
