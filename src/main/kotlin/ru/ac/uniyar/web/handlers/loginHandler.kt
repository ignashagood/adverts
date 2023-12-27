package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.SameSite
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.invalidateCookie
import org.http4k.core.with
import org.http4k.lens.Invalid
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.operations.UserLoginAuthentificationOperation
import ru.ac.uniyar.web.filters.JwtTools
import ru.ac.uniyar.web.models.LoginVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

class ShowLoginHandler(
    private val htmlView: ContextAwareViewRender
) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlView(request) of LoginVM())
    }
}

val autentificationLens = Body.webForm(
    Validator.Feedback,
    loginLens,
    passwordLens
).toLens()

class UserAutentification(
    private val userLoginAuthentificationOperation: UserLoginAuthentificationOperation,
    private val htmlViewModel: ContextAwareViewRender,
    private val jwtTools: JwtTools,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = autentificationLens(request)
        if (form.errors.isNotEmpty()) {
            return Response(Status.OK).with(htmlViewModel(request) of LoginVM(form))
        }
        val username = try {
            userLoginAuthentificationOperation.authentificate(
                loginLens(form),
                passwordLens(form)
            )
        } catch (_: UserLoginAuthentificationOperation.Companion.AuthentificationError) {
            val newErrors = form.errors +
                Invalid(
                    passwordLens.meta.copy(description = "Введите корректные поля имя пользователя и пароль")
                )
            val newForm = form.copy(errors = newErrors)
            return Response(Status.OK).with(htmlViewModel(request) of LoginVM(newForm))
        }
        val token = jwtTools.create(username) ?: return Response(Status.INTERNAL_SERVER_ERROR)
        val authCookie = Cookie("auth_token", token, httpOnly = true, sameSite = SameSite.Strict)
        return Response(Status.FOUND)
            .header("Location", "/applications")
            .cookie(authCookie)
    }
}

class UserLogout : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.FOUND).header("Location", "/adverts").invalidateCookie("auth_token")
    }
}
