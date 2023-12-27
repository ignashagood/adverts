package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Service

data class NewAdvertVM(
    val specialistParameter: String,
    val servicesList: List<Service>,
    val userCity: String,
    val service: String = "",
    val advert: String = "",
    val description: String = "",
    val webForm: WebForm = WebForm(),
) : ViewModel {
    val serviceName =
        if (webForm.fields["service_name"]?.get(0) == null)
            service
        else webForm.fields["service_name"]?.get(0)
    val advertName =
        if (webForm.fields["advert_name"]?.get(0) == null) advert
        else webForm.fields["advert_name"]?.get(0)
    val advertDescription =
        if (webForm.fields["description"]?.get(0) == null) description
        else webForm.fields["description"]?.get(0)
}
