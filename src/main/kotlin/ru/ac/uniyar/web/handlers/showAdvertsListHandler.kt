package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.operations.FetchAdvertsListOperation
import ru.ac.uniyar.domain.operations.FetchCitiesListOperation
import ru.ac.uniyar.domain.operations.FetchCityOperation
import ru.ac.uniyar.domain.operations.FetchServicesListOperation
import ru.ac.uniyar.domain.operations.GetPagesOfAdvertsOperation
import ru.ac.uniyar.web.models.GroupOfAdvertsVM
import ru.ac.uniyar.web.models.Paginator
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showAdvertsList(
    htmlViewModel: ContextAwareViewRender,
    getPagesOfAdvertsOperation: GetPagesOfAdvertsOperation,
    fetchAdvertsListOperation: FetchAdvertsListOperation,
    fetchServicesListOperation: FetchServicesListOperation,
    fetchCitiesListOperation: FetchCitiesListOperation,
    fetchCityOperation: FetchCityOperation,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = handler@{
    val currentUser = lensOrNull(currentUserLens, it)
    val usersCity =
        if (currentUser != null) fetchCityOperation.fetch(currentUser.city)?.cityName!!
        else ""
    val cityName = if (searchCityNameLens(it) == "") usersCity else searchCityNameLens(it)
    val currentPage = pageNumberLens(it)
    val serviceName = searchServiceNameLens(it)
    val specialistName = searchInitialsLens(it)
    val amountOfPages = getPagesOfAdvertsOperation.getPagesAmount(specialistName, cityName, serviceName)
    val listOfAdverts =
        fetchAdvertsListOperation.fetchListWithFilters(
            currentPage, specialistName, cityName, serviceName
        )
    Response(Status.OK).with(
        htmlViewModel(it) of
            GroupOfAdvertsVM(
                listOfAdverts,
                fetchServicesListOperation.fetchList(),
                fetchCitiesListOperation.fetchList(),
                serviceName,
                specialistName,
                cityName,
                Paginator(it.uri, currentPage, amountOfPages)
            )
    )
}
