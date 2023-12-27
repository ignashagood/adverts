package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object SpecialistsList : Table<Nothing>("specialists") {
    val specialist_id = int("specialist_id").primaryKey()
    val locale = varchar("locale")
    val initials = varchar("initials")
    val education = varchar("education")
    val phoneNumber = varchar("phone_number")
    val certificates = varchar("certificates")
    val login = varchar("login")
    val workExperience = varchar("work_experience")
    val detailContacts = varchar("detail_contacts")
    val password = varchar("password")
    val city = int("city")
    val roleID = int("role_id")
}
