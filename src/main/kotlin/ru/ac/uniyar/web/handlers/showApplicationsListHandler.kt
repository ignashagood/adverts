package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.string
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.FetchApplicationsAmountOperation
import ru.ac.uniyar.domain.operations.FetchApplicationsOperation
import ru.ac.uniyar.domain.operations.FetchServiceOperation
import ru.ac.uniyar.domain.operations.FetchServicesListOperation
import ru.ac.uniyar.domain.operations.FetchSpecialistOperation
import ru.ac.uniyar.web.models.GroupOfApplicationsVM
import ru.ac.uniyar.web.models.Paginator
import ru.ac.uniyar.web.templates.ContextAwareViewRender

val statusLens = Query.string().defaulted("status", "")

fun searchStatusLens(request: Request) = lensOrDefault(
    statusLens,
    request,
    ""
)

fun showApplicationsList(
    htmlViewModel: ContextAwareViewRender,
    fetchServicesListOperation: FetchServicesListOperation,
    fetchApplicationsOperation: FetchApplicationsOperation,
    fetchApplicationsAmountOperation: FetchApplicationsAmountOperation,
    fetchServiceOperation: FetchServiceOperation,
    fetchSpecialistOperation: FetchSpecialistOperation,
    permissionsLens: RequestContextLens<RoleRules>,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = handler@{
    val permissions = permissionsLens(it)
    if (!permissions.applicationsList)
        return@handler Response(Status.UNAUTHORIZED)
    val currentUser = lensOrNull(currentUserLens, it)

    val currentPage = pageNumberLens(it)
    val serviceName = searchServiceNameLens(it)
    val status = searchStatusLens(it)
    val currentUserInitials =
        if (currentUser?.roleID == 2)
            ""
        else currentUser?.initials!!
    val amountOfPages = (
        fetchApplicationsAmountOperation.fetchAmount(
            currentUserInitials,
            serviceName
        ) - 1
        ) / 6 + 1
    val listOfApplications =
        fetchApplicationsOperation.fetchList(
            currentPage,
            currentUserInitials,
            serviceName,
            status
        )
    Response(Status.OK).with(
        htmlViewModel(it) of
            GroupOfApplicationsVM(
                listOfApplications,
                fetchServicesListOperation.fetchList(),
                serviceName,
                status,
                fetchServiceOperation,
                fetchSpecialistOperation,
                Paginator(it.uri, currentPage, amountOfPages)
            )
    )
}
