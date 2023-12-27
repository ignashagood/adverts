package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Advert
import ru.ac.uniyar.domain.db.essence.Specialist

data class SpecialistVM(
    val specialist: Specialist,
    val advertsList: List<Advert>,
    val paginator: Paginator,
    val cityName: String
) : ViewModel {
    val hasData: Boolean = advertsList.isNotEmpty()
}
