package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.City

data class GroupOfCitiesVM(
    val citiesList: List<City>,
    val cityNameValue: String,
    val paginator: Paginator
) : ViewModel {
    val hasData: Boolean = citiesList.isNotEmpty()
}
