package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.routing.path
import ru.ac.uniyar.domain.operations.FetchCityOperation
import ru.ac.uniyar.domain.operations.FetchServicesInCityOperation
import ru.ac.uniyar.web.models.CityVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showCity(
    htmlViewModel: ContextAwareViewRender,
    fetchServicesInCityOperation: FetchServicesInCityOperation,
    fetchCityOperation: FetchCityOperation
): HttpHandler = { request ->
    val requestID = request.path("city_id")?.toIntOrNull()
    requestID
        ?.let {
            fetchServicesInCityOperation.fetchList(it)
        }
        ?.let {
            Response(Status.OK).with(
                htmlViewModel(request) of
                    CityVM(
                        fetchCityOperation.fetch(requestID)!!, it,
                    )
            )
        }
        ?: Response(Status.BAD_REQUEST)
}
