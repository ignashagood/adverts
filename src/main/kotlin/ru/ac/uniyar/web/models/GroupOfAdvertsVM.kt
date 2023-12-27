package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Advert
import ru.ac.uniyar.domain.db.essence.City
import ru.ac.uniyar.domain.db.essence.Service

data class GroupOfAdvertsVM(
    val advertsList: List<Advert>,
    val servicesList: List<Service>,
    val citiesList: List<City>,
    val serviceNameValue: String,
    val specialistNameValue: String,
    val cityNameValue: String,
    val paginator: Paginator
) : ViewModel {
    val hasData: Boolean = advertsList.isNotEmpty()
}
