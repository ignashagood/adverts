package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.http4k.routing.path
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.FetchAdvertOperation
import ru.ac.uniyar.web.models.AdvertVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showAdvert(
    htmlViewModel: ContextAwareViewRender,
    fetchAdvertOperation: FetchAdvertOperation,
    permissionsLens: RequestContextLens<RoleRules>,
): HttpHandler = handler@{ request ->
    val permissions = permissionsLens(request)
    if (!permissions.advertPage)
        return@handler Response(Status.UNAUTHORIZED)
    request
        .path("id")
        ?.toIntOrNull()
        ?.let {
            fetchAdvertOperation.fetch(it)
        }
        ?.let {
            Response(Status.OK).with(htmlViewModel(request) of AdvertVM(it))
        }
        ?: Response(Status.BAD_REQUEST)
}
