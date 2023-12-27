package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.City
import ru.ac.uniyar.domain.db.essence.Service

data class CityVM(
    val city: City,
    val pairsList: List<Pair<Service, Int>>,
) : ViewModel {
    val hasData = pairsList.isNotEmpty()
}
