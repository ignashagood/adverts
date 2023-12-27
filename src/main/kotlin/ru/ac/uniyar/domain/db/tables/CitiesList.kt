package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object CitiesList : Table<Nothing>("cities") {
    val city_id = int("city_id").primaryKey()
    val city_name = varchar("city_name")
}
