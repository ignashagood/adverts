package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

class LoginVM(
    val form: WebForm = WebForm()
) : ViewModel {
    val userName = form.fields["userName"]?.get(0)
    val userPassword = form.fields["userPassword"]?.get(0)
}
