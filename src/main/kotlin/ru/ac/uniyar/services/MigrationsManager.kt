package ru.ac.uniyar.services

import org.flywaydb.core.Flyway
import ru.ac.uniyar.config.DatabaseConfig

fun performMigrations(databaseConfig: DatabaseConfig) {
    val flyway = Flyway
        .configure()
        .locations("ru/ac/uniyar/domain/db/migrations")
        .validateMigrationNaming(true)
        .dataSource(databaseConfig.stringJDBC(), databaseConfig.dbUsername, databaseConfig.dbPassword)
        .load()
    flyway.migrate()
}
