package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ServicesList : Table<Nothing>("services") {
    val service_id = int("service_id").primaryKey()
    val locale = varchar("locale")
    val service_name = varchar("service_name")
}
