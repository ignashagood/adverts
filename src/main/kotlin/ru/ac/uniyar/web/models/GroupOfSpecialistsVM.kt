package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Specialist

data class GroupOfSpecialistsVM(
    val specialistsList: List<Specialist>,
    val initialsSearchValue: String,
    val phoneNumberSearchValue: String,
    val paginator: Paginator
) : ViewModel {
    val hasData: Boolean = specialistsList.isNotEmpty()
}
