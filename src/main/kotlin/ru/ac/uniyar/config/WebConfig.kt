package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int

class WebConfig(val port: Int) {
    companion object {
        val portLens = EnvironmentKey.int().required("web.port")
        fun fromEnvironment(environment: Environment): WebConfig =
            WebConfig(portLens(environment))
        val defaultEnv = Environment.defaults(
            portLens of 1515,
        )
    }
}
