package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.essence.Application
import ru.ac.uniyar.domain.db.tables.ApplicationsList

class AddApplicationOperation(
    private val database: Database
) {
    fun addApplication(application: Application) =
        database
            .insert(ApplicationsList) {
                set(it.serviceID, application.serviceID)
                set(it.contacts, application.contacts)
                set(it.education, application.education)
                set(it.certificates, application.certificates)
                set(it.workingExperience, application.workingExperience)
                set(it.specialistID, application.specialistID)
                set(it.status, application.status)
                set(it.comment, application.comment)
            }
}
