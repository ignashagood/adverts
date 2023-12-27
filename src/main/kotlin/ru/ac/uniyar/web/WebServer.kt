package ru.ac.uniyar.web

import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.ktorm.database.Database
import ru.ac.uniyar.config.AppConfig
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.OperationHolder
import ru.ac.uniyar.web.filters.JwtTools
import ru.ac.uniyar.web.filters.authenticationFilter
import ru.ac.uniyar.web.filters.authorizationFilter
import ru.ac.uniyar.web.filters.badResponseFilter
import ru.ac.uniyar.web.templates.ContextAwarePebbleTemplates
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun startWebServer(
    database: Database,
    appConfig: AppConfig,
): Http4kServer {
    val operationHolder = OperationHolder(
        database,
        appConfig.saltConfig.salt
    )
    val context = RequestContexts()
    val jwtTools = JwtTools(appConfig.saltConfig.salt, "ru.ac.uniyar.WebApplication")
    val currentUserLens: RequestContextLens<Specialist?> = RequestContextKey.optional(context, name = "specialist")
    val permissionsLens: RequestContextLens<RoleRules> =
        RequestContextKey.required(context, name = "permissions")
//    val permissionsLens = RequestContextKey.required<RoleRules>(context)
    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender.withContentType(renderer, ContentType.TEXT_HTML)
    val htmlViewWithContext = htmlView
        .associateContextLens("currentUser", currentUserLens)
        .associateContextLens("permissions", permissionsLens)

    val authorizedApp = authenticationFilter(
        currentUserLens,
        operationHolder.fetchUserViaUsernameOperation,
        jwtTools.subject,
    ).then(
        authorizationFilter(
            currentUserLens,
            permissionsLens,
            operationHolder.fetchIsRoleValidOperation
        )
    )
    val printingApp: HttpHandler =
        ServerFilters
            .InitialiseRequestContext(context)
            .then(authorizedApp)
            .then(
                badResponseFilter((htmlViewWithContext))
            )
            .then(
                app(
                    htmlViewWithContext,
                    operationHolder,
                    currentUserLens,
                    permissionsLens,
                    jwtTools
                )
            )
    return printingApp.asServer(Undertow(appConfig.webConf.port)).start()
}
