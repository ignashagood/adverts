package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object RoleTable : Table<Nothing>("role") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val advertPage = boolean("advert_page")
    val changingCity = boolean("changing_city")
    val sendingApplication = boolean("sending_application")
    val applicationsList = boolean("applications_list")
    val applicationsPage = boolean("application_page")
    val agreement = boolean("agreement")
    val appendAdvert = boolean("append_advert")
    val editAdvert = boolean("edit_advert")
}
