package ru.ac.uniyar.services

import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import ru.ac.uniyar.config.DatabaseConfig

fun connectToDatabase(databaseConfig: DatabaseConfig) =
    Database.connect(
        url = databaseConfig.stringJDBC(),
        driver = "org.h2.Driver",
        user = databaseConfig.dbUsername,
        dialect = MySqlDialect(),
    )
