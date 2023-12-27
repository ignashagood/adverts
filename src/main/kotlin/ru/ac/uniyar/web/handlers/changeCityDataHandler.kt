package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.EditUserCityOperation
import ru.ac.uniyar.domain.operations.FetchCitiesListOperation
import ru.ac.uniyar.domain.operations.FetchCityIDByCityNameOperation
import ru.ac.uniyar.web.models.ChangeDataVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender
val changingCityNameFormLens = Body.webForm(
    Validator.Feedback,
    cityNameLens
).toLens()

fun showChangeForm(
    htmlViewModel: ContextAwareViewRender,
    fetchCitiesListOperation: FetchCitiesListOperation,
    permissionsLens: RequestContextLens<RoleRules>,
): HttpHandler = handler@{
    val permissions = permissionsLens(it)
    if (!permissions.changingCity)
        return@handler Response(Status.UNAUTHORIZED)
    val citiesList = fetchCitiesListOperation.fetchList()
    Response(Status.OK).with(htmlViewModel(it) of ChangeDataVM(citiesList))
}

fun editCity(
    fetchCityIDByCityNameOperation: FetchCityIDByCityNameOperation,
    currentUserLens: BiDiLens<Request, Specialist?>,
    editUserCityOperation: EditUserCityOperation
): HttpHandler = { request ->
    // Если мы запретили на вход эту страницу, то проверку на добавление делать не стоит, тк будет излишне
    val changeCityForm = changingCityNameFormLens(request)
    val cityName = cityNameLens(changeCityForm)
    val currentUser = lensOrNull(currentUserLens, request)
    try {
        if (currentUser != null) {
            editUserCityOperation.edit(
                fetchCityIDByCityNameOperation.fetchID(cityName),
                currentUser.specialistId
            )
        }
        Response(Status.FOUND).header("Location", "/specialists/${currentUser?.specialistId}")
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST).header("Location", "/specialists/${currentUser?.specialistId}")
    }
}
