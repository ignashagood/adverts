package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Validator
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.operations.AddSpecialistOperation
import ru.ac.uniyar.domain.operations.FetchCitiesListOperation
import ru.ac.uniyar.domain.operations.FetchCityIDByCityNameOperation
import ru.ac.uniyar.web.models.RegistrationVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender
import java.sql.SQLIntegrityConstraintViolationException

val initialsLens = FormField.nonEmptyString().required("initials", "Инициалы специалиста")
val educationLens = FormField.nonEmptyString().required("education", "Образование специалиста")
val phoneNumberLens = FormField.nonEmptyString().required("phoneNumber", "Номер телефона")
val loginLens = FormField.nonEmptyString().required("userName", "Логин")
val passwordLens = FormField.nonEmptyString().required("userPassword", "Пароль")
val password2Lens = FormField.nonEmptyString().required("checkPassword", "Пароль")
val cityLens = FormField.nonEmptyString().required("usersCity", "Город пользователя")

val specialistLens = Body.webForm(
    Validator.Feedback,
    initialsLens,
    phoneNumberLens,
    loginLens,
    cityLens
).toLens()

fun showNewSpecialistForm(
    htmlViewModel: ContextAwareViewRender,
    fetchCitiesListOperation: FetchCitiesListOperation
): HttpHandler = {
    val citiesList = fetchCitiesListOperation.fetchList()
    Response(Status.OK).with(htmlViewModel(it) of RegistrationVM(citiesList))
}

fun createNewSpecialist(
    htmlViewModel: ContextAwareViewRender,
    addSpecialistOperation: AddSpecialistOperation,
    fetchCitiesListOperation: FetchCitiesListOperation,
    fetchCityIDByCityNameOperation: FetchCityIDByCityNameOperation,
): HttpHandler = { request ->
    var specialistForm = specialistLens(request)
    val passport = lensOrNull(passwordLens, specialistForm)
    val checkPassword = lensOrNull(password2Lens, specialistForm)
    val citiesList = fetchCitiesListOperation.fetchList()
    val cityName = cityLens(specialistForm)
    if (
        passport.isNullOrEmpty() ||
        checkPassword.isNullOrEmpty() ||
        passport != checkPassword
    ) {
        val newErrors = specialistForm.errors +
            Invalid(passwordLens.meta.copy(description = "Пароли не совпадают"))
        specialistForm = specialistForm.copy(errors = newErrors)
    }
    if (specialistForm.errors.isEmpty()) {
        try {
            addSpecialistOperation.addSpecialist(
                initialsLens(specialistForm),
                "NOT A SPECIALIST YET",
                phoneNumberLens(specialistForm),
                "NOT A SPECIALIST YET",
                "NOT A SPECIALIST YET",
                "NOT A SPECIALIST YET",
                loginLens(specialistForm),
                passwordLens(specialistForm),
                fetchCityIDByCityNameOperation.fetchID(cityName)
            )
            Response(Status.FOUND).header("Location", "/adverts")
        } catch (_: SQLIntegrityConstraintViolationException) {
            val newErrors = specialistForm.errors +
                Invalid(passwordLens.meta.copy(description = "Логин занят"))
            specialistForm = specialistForm.copy(errors = newErrors)
            Response(Status.OK).with(
                htmlViewModel(request) of
                    RegistrationVM(
                        citiesList,
                        specialistForm
                    )
            )
        }
    } else {
        Response(Status.BAD_REQUEST).with(
            htmlViewModel(request) of RegistrationVM(citiesList, specialistForm)
        )
    }
}
