package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment

class AppConfig(
    val webConf: WebConfig = WebConfig.fromEnvironment(appEnv),
    val databaseConfig: DatabaseConfig = DatabaseConfig.fromEnvironmentDB(appEnv),
    val saltConfig: SaltConfig = SaltConfig.fromEnvironmentSalt(appEnv)
) {
    companion object {
        val appEnv = Environment.fromResource("/ru/ac/uniyar/config/app.properties") overrides
            Environment.JVM_PROPERTIES overrides
            Environment.ENV overrides
            WebConfig.defaultEnv overrides
            DatabaseConfig.defaultEnv overrides
            SaltConfig.defaultEnv

        fun readConfiguration(): AppConfig = AppConfig()
    }
}
