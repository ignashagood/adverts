package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.essence.Application
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.SpecialistsList

data class AgreeApplicationOperation(
    private val database: Database
) {
    fun agreeApplication(application: Application) {
        database
            .update(ApplicationsList) {
                set(it.status, "Подтверждена")
                where {
                    it.id eq application.id
                }
            }
        database
            .update(SpecialistsList) {
                set(it.certificates, application.certificates)
                set(it.education, application.education)
                set(it.detailContacts, application.contacts)
                set(it.workExperience, application.workingExperience)
                set(it.roleID, 3)
                where {
                    it.specialist_id eq application.specialistID
                }
            }
    }
}
