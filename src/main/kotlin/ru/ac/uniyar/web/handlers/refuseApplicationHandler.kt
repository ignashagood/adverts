package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import org.http4k.routing.path
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.FetchApplicationOperation
import ru.ac.uniyar.domain.operations.RefuseApplicationOperation
import ru.ac.uniyar.web.models.RefuseApplicationVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

val commentLens = FormField.nonEmptyString().required("comment", "Комментарий отказа")
val refuseFormLens = Body.webForm(
    Validator.Feedback,
    commentLens
).toLens()
fun refuseApplication(
    htmlViewModel: ContextAwareViewRender,
    permissionsLens: RequestContextLens<RoleRules>,
): HttpHandler = handler@{ request ->
    val permissions = permissionsLens(request)
    if (!permissions.agreement)
        return@handler Response(Status.UNAUTHORIZED)
    Response(Status.OK).with(
        htmlViewModel(request) of
            RefuseApplicationVM()
    )
}

fun makeRefuse(
    htmlViewModel: ContextAwareViewRender,
    fetchApplicationOperation: FetchApplicationOperation,
    refuseApplicationOperation: RefuseApplicationOperation
): HttpHandler = { request ->
    val application =
        request
            .path("id")
            ?.toIntOrNull()
            ?.let {
                fetchApplicationOperation.fetch(it)
            }!!
    val refuseForm = refuseFormLens(request)
    try {
        val comment = commentLens(refuseForm)
        if (refuseForm.errors.isEmpty()) {
            refuseApplicationOperation.refuseApplication(
                application.id,
                comment
            )
            Response(Status.FOUND).header("Location", "/applications")
        } else {
            Response(Status.BAD_REQUEST)
                .with(
                    htmlViewModel(request) of
                        RefuseApplicationVM(refuseForm)
                )
        }
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
            .with(
                htmlViewModel(request) of
                    RefuseApplicationVM(refuseForm)
            )
    }
}
