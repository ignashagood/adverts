package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextLens
import org.http4k.routing.path
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.FetchAdvertOperation
import ru.ac.uniyar.domain.operations.FetchCityOperation
import ru.ac.uniyar.domain.operations.FetchSpecialistsServicesOperation
import ru.ac.uniyar.web.models.NewAdvertVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun editAdvert(
    htmlViewModel: ContextAwareViewRender,
    fetchSpecialistsServicesOperation: FetchSpecialistsServicesOperation,
    fetchAdvert: FetchAdvertOperation,
    fetchCityOperation: FetchCityOperation,
    permissionsLens: RequestContextLens<RoleRules>,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = handler@{ request ->
    val permissions = permissionsLens(request)
    if (!permissions.appendAdvert)
        return@handler Response(Status.UNAUTHORIZED)
    val currentUser = currentUserLens(request)!!
    val advert =
        request
            .path("id")
            ?.toIntOrNull()
            ?.let {
                fetchAdvert.fetch(it)
            }!!
    val serviceList = fetchSpecialistsServicesOperation.fetchList(currentUser.specialistId)
    val newAdvertForm =
        NewAdvertVM(
            currentUser.initials,
            serviceList,
            fetchCityOperation.fetch(currentUser.city)?.cityName!!,
            advert.serviceName,
            advert.advertName,
            advert.description
        )
    Response(Status.OK).with(htmlViewModel(request) of newAdvertForm)
}
