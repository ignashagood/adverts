package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.City

class RegistrationVM(
    val citiesList: List<City>,
    val form: WebForm = WebForm()
) : ViewModel {
    val initials = form.fields["initials"]?.get(0)
    val education = form.fields["education"]?.get(0)
    val phoneNumber = form.fields["phoneNumber"]?.get(0)
    val userName = form.fields["userName"]?.get(0)
    val userPassword = form.fields["userPassword"]?.get(0)
    val checkPassword = form.fields["checkPassword"]?.get(0)
    val cityName = form.fields["usersCity"]?.get(0)
}
