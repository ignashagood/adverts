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
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.AgreeApplicationOperation
import ru.ac.uniyar.domain.operations.FetchApplicationOperation
import ru.ac.uniyar.domain.operations.FetchServiceOperation
import ru.ac.uniyar.domain.operations.FetchSpecialistOperation
import ru.ac.uniyar.web.models.ApplicationVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

val decisionLens = FormField.nonEmptyString().required("decision", "Приговор")
val decisionFormLens = Body.webForm(
    Validator.Feedback,
    decisionLens
).toLens()
fun showApplication(
    htmlViewModel: ContextAwareViewRender,
    fetchApplicationOperation: FetchApplicationOperation,
    fetchServiceOperation: FetchServiceOperation,
    fetchSpecialistOperation: FetchSpecialistOperation,
    permissionsLens: RequestContextLens<RoleRules>,
    currentUserLens: BiDiLens<Request, Specialist?>,
): HttpHandler = handler@{ request ->
    val permissions = permissionsLens(request)
    if (!permissions.applicationsPage)
        return@handler Response(Status.UNAUTHORIZED)
    val currentUser = currentUserLens(request)!!
    request
        .path("id")
        ?.toIntOrNull()
        ?.let {
            fetchApplicationOperation.fetch(it)
        }
        ?.let {
            if (it.specialistID != currentUser.specialistId && currentUser.roleID != 2)
                return@handler Response(Status.UNAUTHORIZED)
            Response(Status.OK).with(
                htmlViewModel(request) of
                    ApplicationVM(
                        it,
                        fetchServiceOperation.fetch(it.serviceID)?.serviceName!!,
                        fetchSpecialistOperation.fetch(it.specialistID)?.initials!!
                    )
            )
        }
        ?: Response(Status.BAD_REQUEST)
}

fun makeDecision(
    fetchApplicationOperation: FetchApplicationOperation,
    agreeApplicationOperation: AgreeApplicationOperation
): HttpHandler = { request ->
    val application =
        request
            .path("id")
            ?.toIntOrNull()
            ?.let {
                fetchApplicationOperation.fetch(it)
            }!!
    val decisionForm = decisionFormLens(request)
    val chosenDecision = decisionLens(decisionForm)
    try {
        if (chosenDecision.toInt() == 1) {
            agreeApplicationOperation.agreeApplication(application)
            Response(Status.FOUND).header("Location", "/applications")
        } else {
            Response(Status.FOUND).header("Location", "/refuse/${application.id}")
        }
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST).header("Location", "/applications/${application.id}")
    }
}
