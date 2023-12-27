package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import org.http4k.routing.path
import ru.ac.uniyar.domain.db.essence.Advert
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.AddAdvertOperation
import ru.ac.uniyar.domain.operations.FetchAdvertsIDsOperation
import ru.ac.uniyar.domain.operations.FetchApplicationByUserAndService
import ru.ac.uniyar.domain.operations.FetchCityOperation
import ru.ac.uniyar.domain.operations.FetchServiceIDOperation
import ru.ac.uniyar.domain.operations.FetchServiceOperation
import ru.ac.uniyar.domain.operations.FetchSpecialistsServicesOperation
import ru.ac.uniyar.domain.operations.UpdateAdvertOperation
import ru.ac.uniyar.web.models.NewAdvertVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

val serviceNameLens = FormField.nonEmptyString().required("service_name", "Имя услуги")
val advertNameLens = FormField.nonEmptyString().required("advert_name", "Имя объявления")
val cityNameLens = FormField.nonEmptyString().required("city_name", "Имя города")
val descriptionLens = FormField.nonEmptyString().required("description", "Описание объявления")
val advertLens = Body.webForm(
    Validator.Feedback,
    serviceNameLens,
    advertNameLens,
    descriptionLens,
).toLens()

fun showNewAdvertForm(
    htmlViewModel: ContextAwareViewRender,
    fetchSpecialistsServicesOperation: FetchSpecialistsServicesOperation,
    fetchCityOperation: FetchCityOperation,
    permissionsLens: RequestContextLens<RoleRules>,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = handler@{
    val permissions = permissionsLens(it)
    if (!permissions.appendAdvert)
        return@handler Response(Status.UNAUTHORIZED)
    val currentUser = currentUserLens(it)!!
    val serviceList = fetchSpecialistsServicesOperation.fetchList(currentUser.specialistId)
    val newAdvertForm =
        NewAdvertVM(
            currentUser.initials,
            serviceList,
            fetchCityOperation.fetch(currentUser.city)?.cityName!!,
        )
    Response(Status.OK).with(htmlViewModel(it) of newAdvertForm)
}

fun createNewAdvert(
    htmlViewModel: ContextAwareViewRender,
    fetchServiceOperation: FetchServiceOperation,
    fetchCityOperation: FetchCityOperation,
    fetchSpecialistsServicesOperation: FetchSpecialistsServicesOperation,
    addAdvertOperation: AddAdvertOperation,
    fetchApplicationByUserAndService: FetchApplicationByUserAndService,
    fetchServiceIDOperation: FetchServiceIDOperation,
    fetchAdvertsIDsOperation: FetchAdvertsIDsOperation,
    updateAdvertOperation: UpdateAdvertOperation,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = { request ->
    val currentUser = currentUserLens(request)!!
    val advertID = request.path("id")?.toIntOrNull()
    val advertForm = advertLens(request)
    try {
        val serviceName = serviceNameLens(advertForm)
        val advertName = advertNameLens(advertForm)
        val description = descriptionLens(advertForm)
        val application = fetchApplicationByUserAndService.fetch(
            currentUser.specialistId,
            fetchServiceIDOperation.fetchID(serviceName)
        )!!
        if (advertForm.errors.isEmpty()) {
            val advert = Advert(
                fetchServiceOperation.fetchID(serviceName),
                serviceName,
                advertName,
                currentUser.city,
                fetchCityOperation.fetch(currentUser.city)?.cityName!!,
                descriptionLens(advertForm),
                currentUser.specialistId,
                currentUser.initials,
                application.contacts,
                application.education,
                application.certificates,
                application.workingExperience,
                application.id,
            )
            if (advertID != null) {
                if (
                    fetchAdvertsIDsOperation.fetchList().indexOf(advertID) != -1
                ) {
                    advert.advertId = advertID
                    updateAdvertOperation.update(advert)
                }
            } else {
                addAdvertOperation.addAdvert(advert)
            }

            Response(Status.FOUND).header("Location", "/adverts")
        } else {
            Response(Status.BAD_REQUEST)
                .with(
                    htmlViewModel(request) of
                        NewAdvertVM(
                            currentUser.initials,
                            fetchSpecialistsServicesOperation.fetchList(currentUser.specialistId),
                            fetchCityOperation.fetch(currentUser.city)?.cityName!!,
                            serviceName,
                            advertName,
                            description,
                            advertForm
                        )
                )
        }
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
            .with(
                htmlViewModel(request) of
                    NewAdvertVM(
                        currentUser.initials,
                        fetchSpecialistsServicesOperation.fetchList(currentUser.specialistId),
                        fetchCityOperation.fetch(currentUser.city)?.cityName!!,
                        "",
                        "",
                        "",
                        advertForm
                    )
            )
    }
}
