package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.City

data class ChangeDataVM(
    val citiesList: List<City>,
    val form: WebForm = WebForm()
) : ViewModel {
    val cityName = form.fields["city_name"]?.get(0)
}
