package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.string
import ru.ac.uniyar.domain.operations.*
import ru.ac.uniyar.web.models.GroupOfServicesVM
import ru.ac.uniyar.web.models.Paginator
import ru.ac.uniyar.web.templates.ContextAwareViewRender

val serviceName = Query.string().defaulted("service_name", "")

fun searchServiceNameLens(request: Request) = lensOrDefault(
    serviceName,
    request,
    ""
)

fun showServicesList(
    htmlViewModel: ContextAwareViewRender,
    getPagesOfServicesAmountOperation: GetPagesOfServicesOperation,
    fetchServicesOperation: FetchServicesOperation,
    fetchServicesAmountOperation: FetchServicesAmountOperation,
    fetchAdvertsAmountOperation: FetchAdvertsAmountOperation,
    fetchServicesListOperation: FetchServicesListOperation,
    fetchCitiesListOperation: FetchCitiesListOperation
): HttpHandler = {
    val currentPage = pageNumberLens(it)
    val serviceName = searchServiceNameLens(it)
    val cityName = searchCityNameLens(it)
    val amountOfPages = getPagesOfServicesAmountOperation.getPagesAmount(serviceName, cityName)
    val listOfServices =
        fetchServicesOperation.fetchListWithFilters(currentPage, serviceName, cityName)
    val servicesList = fetchServicesListOperation.fetchList()
    val servicesAmount = fetchServicesAmountOperation.getServicesAmount(serviceName, cityName)
    val citiesList = fetchCitiesListOperation.fetchList()
    Response(Status.OK).with(
        htmlViewModel(it) of
            GroupOfServicesVM(
                listOfServices,
                cityName,
                serviceName,
                servicesList,
                servicesAmount,
                citiesList,
                fetchAdvertsAmountOperation,
                Paginator(it.uri, currentPage, amountOfPages)
            )
    )
}
