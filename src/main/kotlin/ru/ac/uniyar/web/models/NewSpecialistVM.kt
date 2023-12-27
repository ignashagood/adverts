package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

data class NewSpecialistVM(
    val webForm: WebForm = WebForm()
) : ViewModel {
    val initials = webForm.fields["initials"]?.get(0)
    val education = webForm.fields["education"]?.get(0)
    val phoneNumber = webForm.fields["phoneNumber"]?.get(0)
}
