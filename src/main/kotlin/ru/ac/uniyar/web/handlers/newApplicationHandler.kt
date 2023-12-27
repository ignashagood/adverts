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
import ru.ac.uniyar.domain.db.essence.Application
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.AddApplicationOperation
import ru.ac.uniyar.domain.operations.FetchAvailableServicesOperation
import ru.ac.uniyar.domain.operations.FetchServiceIDOperation
import ru.ac.uniyar.web.models.NewApplicationVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender
val certificatesLens = FormField.nonEmptyString().required("certificates", "Удостоверения")
val workExperienceLens = FormField.nonEmptyString().required("workingExperience", "Опыт работы")
val detailContactsLens = FormField.nonEmptyString().required("detail_contacts", "Детальные контакты")
val applicationFormLens = Body.webForm(
    Validator.Feedback,
    serviceNameLens,
    certificatesLens,
    workExperienceLens,
    detailContactsLens,
    educationLens
).toLens()
fun showNewApplicationForm(
    htmlViewModel: ContextAwareViewRender,
    fetchAvailableServicesOperation: FetchAvailableServicesOperation,
    permissionsLens: RequestContextLens<RoleRules>,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = handler@{
    val permissions = permissionsLens(it)
    if (!permissions.sendingApplication)
        return@handler Response(Status.UNAUTHORIZED)
    val currentUser = currentUserLens(it)!!
    val servicesList = fetchAvailableServicesOperation.fetchAvailableServices(
        currentUser.specialistId
    )
    val newApplicationForm =
        if (currentUser.roleID == 3)
            NewApplicationVM(
                servicesList,
                currentUser.detailContacts,
                currentUser.education,
                currentUser.certificates,
                currentUser.workingExperience
            )
        else
            NewApplicationVM(servicesList)
    Response(Status.OK).with(htmlViewModel(it) of newApplicationForm)
}

fun createNewApplication(
    htmlViewModel: ContextAwareViewRender,
    fetchAvailableServicesOperation: FetchAvailableServicesOperation,
    addApplicationOperation: AddApplicationOperation,
    fetchServiceIDOperation: FetchServiceIDOperation,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = { request ->
    val applicationsForm = applicationFormLens(request)
    val currentUser = currentUserLens(request)!!
    val servicesList = fetchAvailableServicesOperation.fetchAvailableServices(
        currentUser.specialistId
    )
    try {
        if (applicationsForm.errors.isEmpty()) {
            val application = Application(
                fetchServiceIDOperation.fetchID(serviceNameLens(applicationsForm)),
                detailContactsLens(applicationsForm),
                educationLens(applicationsForm),
                certificatesLens(applicationsForm),
                workExperienceLens(applicationsForm),
                currentUser.specialistId,
                "Новая",
                "NO COMMENTS"
            )
            addApplicationOperation.addApplication(application)
            Response(Status.FOUND).header("Location", "/applications")
        } else {
            Response(Status.BAD_REQUEST)
                .with(
                    htmlViewModel(request) of
                        NewApplicationVM(
                            servicesList,
                            "",
                            "",
                            "",
                            "",
                            applicationsForm
                        )
                )
        }
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
            .with(
                htmlViewModel(request) of
                    NewApplicationVM(
                        servicesList,
                        "",
                        "",
                        "",
                        "",
                        applicationsForm
                    )
            )
    }
}
