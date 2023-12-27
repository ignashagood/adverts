package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.FetchIsRoleValidOperation

fun authorizationFilter(
    currentStudent: RequestContextLens<Specialist?>,
    permissionsLens: RequestContextLens<RoleRules>,
    fetchIsRoleValidOperation: FetchIsRoleValidOperation,
): Filter = Filter { next: HttpHandler ->
    { request: Request ->
        val permissions = currentStudent(request)?.let {
            fetchIsRoleValidOperation.fetch(it.roleID)
        } ?: RoleRules.ANONYMOUS_ROLE
        val authorizedRequest = request.with(permissionsLens of permissions)
        next(authorizedRequest)
    }
}
