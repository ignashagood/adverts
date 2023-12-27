package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status

fun respondWithPong(): HttpHandler = {
    Response(Status.OK).body("pong")
}
