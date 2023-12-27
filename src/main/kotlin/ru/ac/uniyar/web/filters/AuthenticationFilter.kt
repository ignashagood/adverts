package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.operations.FetchUserViaUsernameOperation

fun authenticationFilter(
    currentUser: RequestContextLens<Specialist?>,
    fetchUserViaUsernameOperation: FetchUserViaUsernameOperation,
    getUser: (String) -> String?,
): Filter = Filter { next: HttpHandler ->
    { request: Request ->
        val requestWithUser = request.cookie("auth_token")?.value?.let { token ->
            getUser(token)
        }?.let { username ->
            fetchUserViaUsernameOperation.fetch(username)
        }?.let { user ->
            request.with(currentUser of user)
        } ?: request
        next(requestWithUser)
    }
}
