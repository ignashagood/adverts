package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

class RefuseApplicationVM(
    val form: WebForm = WebForm()
) : ViewModel {
    val comment = form.fields["comment"]?.get(0)
}
