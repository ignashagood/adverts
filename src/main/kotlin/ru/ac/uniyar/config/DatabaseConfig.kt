package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string

class DatabaseConfig(
    val dbHost: String,
    val dbPort: Int,
    val dbName: String,
    val dbUsername: String,
    val dbPassword: String
) {
    companion object {
        val hostLens = EnvironmentKey.string().required("db.host")
        val portLens = EnvironmentKey.int().required("db.port")
        val nameLens = EnvironmentKey.nonEmptyString().required("db.name")
        val usernameLens = EnvironmentKey.nonEmptyString().required("db.username")
        val passwordLens = EnvironmentKey.string().required("db.password")
        fun fromEnvironmentDB(environment: Environment): DatabaseConfig = DatabaseConfig(
            hostLens(environment),
            portLens(environment),
            nameLens(environment),
            usernameLens(environment),
            passwordLens(environment)
        )
        val defaultEnv: Environment = Environment.defaults(
            hostLens of "localhost",
            portLens of 8082,
            nameLens of "database.h2",
            usernameLens of "sa",
            passwordLens of ""
        )
    }
    fun stringJDBC() = "jdbc:h2:tcp://$dbHost/$dbName"
}
