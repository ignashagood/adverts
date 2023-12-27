package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.City
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.operations.FetchAdvertsAmountOperation

data class GroupOfServicesVM(
    val servicesList: List<Service>,
    val cityNameValue: String,
    val serviceNameValue: String,
    val listOfServices: List<Service>,
    val amountOfServices: Int,
    val citiesList: List<City>,
    val fetchAdvertsAmountOperation: FetchAdvertsAmountOperation,
    val paginator: Paginator
) : ViewModel {
    fun fetchAmountOfAdverts(serviceName: String) =
        if (fetchAdvertsAmountOperation.fetchAdvertsAmount("", "", serviceName) != 0)
            fetchAdvertsAmountOperation.fetchAdvertsAmount("", "", serviceName)
        else 0
    val hasData: Boolean = servicesList.isNotEmpty()
}
