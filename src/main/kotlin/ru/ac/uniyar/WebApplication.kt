package ru.ac.uniyar

import org.flywaydb.core.api.FlywayException
import ru.ac.uniyar.config.AppConfig
import ru.ac.uniyar.services.H2DatabaseManager
import ru.ac.uniyar.services.connectToDatabase
import ru.ac.uniyar.services.performMigrations
import ru.ac.uniyar.web.startWebServer
const val ONE = 1
const val SIX = 6
const val TEN = 10
const val TWELVE = 12

fun main() {
    val config = AppConfig.readConfiguration()
    val h2DatabaseManager = H2DatabaseManager(config.databaseConfig).initialize()
    try {
        performMigrations(config.databaseConfig)
    } catch (e: FlywayException) {
        println(e)
        h2DatabaseManager.stopServers()
        return
    }
    val database = connectToDatabase(config.databaseConfig)

    val webServer = startWebServer(database, config)

    println("Сервер доступен по адресу http://localhost:" + webServer.port())
    println("Веб-интерфейс базы данных доступен по адресу http://localhost:${config.databaseConfig.dbPort}")
    println("Введите любую строку, чтобы завершить работу приложения")
    readlnOrNull()
    webServer.stop()
    h2DatabaseManager.stopServers()
}
