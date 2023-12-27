package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.string
import ru.ac.uniyar.domain.operations.FetchCitiesOperation
import ru.ac.uniyar.domain.operations.GetPagesOfCitiesOperation
import ru.ac.uniyar.web.models.GroupOfCitiesVM
import ru.ac.uniyar.web.models.Paginator
import ru.ac.uniyar.web.templates.ContextAwareViewRender

val cityName = Query.string().defaulted("city_name", "")

fun searchCityNameLens(request: Request): String {
    return lensOrDefault(
        cityName,
        request,
        ""
    )
}

fun showCitiesList(
    htmlViewModel: ContextAwareViewRender,
    getPagesOfCitiesOperation: GetPagesOfCitiesOperation,
    fetchCitiesOperation: FetchCitiesOperation
): HttpHandler = {
    val currentPage = pageNumberLens(it)
    val cityName = searchCityNameLens(it)
    val amountOfPages = getPagesOfCitiesOperation.getAmountOfPages(cityName)
    val listOfCities = fetchCitiesOperation.fetchListWithFilters(currentPage, cityName)
    Response(Status.OK).with(
        htmlViewModel(it) of
            GroupOfCitiesVM(
                listOfCities,
                cityName,
                Paginator(it.uri, currentPage, amountOfPages)
            )
    )
}
