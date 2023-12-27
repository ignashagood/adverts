package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Service

class NewApplicationVM(
    val servicesList: List<Service>,
    private val contacts: String = "",
    private val specialistsEducation: String = "",
    private val specialistsCertificates: String = "",
    private val specialistsWorkingExp: String = "",
    val form: WebForm = WebForm()
) : ViewModel {
    val serviceName = form.fields["service_name"]?.get(0)
    val detailContacts =
        if (contacts == "")
            form.fields["detail_contacts"]?.get(0)
        else contacts
    val education =
        if (specialistsEducation == "")
            form.fields["education"]?.get(0)
        else specialistsEducation
    val certificates =
        if (specialistsCertificates == "")
            form.fields["certificates"]?.get(0)
        else specialistsCertificates
    val workingExperience =
        if (specialistsWorkingExp == "")
            form.fields["workingExperience"]?.get(0)
        else specialistsWorkingExp
}
