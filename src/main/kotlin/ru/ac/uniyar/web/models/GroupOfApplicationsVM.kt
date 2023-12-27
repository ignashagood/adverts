package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Application
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.operations.FetchServiceOperation
import ru.ac.uniyar.domain.operations.FetchSpecialistOperation

data class GroupOfApplicationsVM(
    val applicationsList: List<Application>,
    val servicesList: List<Service>,
    val serviceNameValue: String,
    val status: String,
    val fetchServiceOperation: FetchServiceOperation,
    val fetchSpecialistOperation: FetchSpecialistOperation,
    val paginator: Paginator
) : ViewModel {
    val hasData: Boolean = applicationsList.isNotEmpty()
}
