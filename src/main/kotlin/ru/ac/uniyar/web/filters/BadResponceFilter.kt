package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import ru.ac.uniyar.web.models.ErrorPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun badResponseFilter(
    htmlView: ContextAwareViewRender
) = Filter { next: HttpHandler ->
    { request: Request ->
        val errorPage = ErrorPageVM(request.uri)
        val response = next(request)
        if (
            (response.status.clientError && response.body.length == 0L) ||
            (response.status.serverError) ||
            (response.header("ContentType")) == ""
        ) {
            response.with(htmlView(request) of errorPage)
        } else {
            response
        }
    }
}
