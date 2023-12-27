package ru.ac.uniyar.services

import org.h2.tools.Server
import ru.ac.uniyar.config.DatabaseConfig
import kotlin.concurrent.thread

class H2DatabaseManager(private val databaseConfig: DatabaseConfig) {
    private var tcpServer: Server? = null
    private var webServer: Server? = null
    private val shutdownThread = thread(start = false, name = "") {
        println("Stopping server")
        stopServers()
    }

    fun initialize(): H2DatabaseManager {
        startServers()
        registerShutdownHook()
        return this
    }

    private fun startServers() {
        tcpServer = Server.createTcpServer(
            "-tcpPort", "9092",
            "-baseDir", ".",
            "-ifNotExists",
        ).start()
        webServer = Server.createWebServer(
            "-webPort", databaseConfig.dbPort.toString(),
            "-baseDir", ".",
            "-ifNotExists",
        ).start()
    }

    private fun registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(shutdownThread)
    }

    fun stopServers() {
        tcpServer?.stop()
        tcpServer = null
        webServer?.stop()
        webServer = null
    }
}
