package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ApplicationsList : Table<Nothing>("applications") {
    val id = int("id").primaryKey()
    val serviceID = int("service_id")
    val contacts = varchar("contacts")
    val education = varchar("education")
    val certificates = varchar("certificates")
    val workingExperience = varchar("working_experience")
    val specialistID = int("specialist_id")
    val status = varchar("status")
    val comment = varchar("comment")
}
