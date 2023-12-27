package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object AdvertsList : Table<Nothing>("adverts") {
    val advert_id = int("advert_id").primaryKey()
    val service_id = int("service_id")
    val advert_name = varchar("advert_name")
    val city_id = int("city_id")
    val description = varchar("description")
    val specialist_id = int("specialist_id")
    val application_id = int("application_id")
}
