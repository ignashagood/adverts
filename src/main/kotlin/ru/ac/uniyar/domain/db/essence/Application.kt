package ru.ac.uniyar.domain.db.essence

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.ServicesList
import ru.ac.uniyar.domain.db.tables.SpecialistsList

data class Application(
    val serviceID: Int,
    val contacts: String,
    val education: String,
    val certificates: String,
    val workingExperience: String,
    val specialistID: Int = 2,
    val status: String,
    val comment: String,
    val id: Int = 0
) {
    companion object {
        fun fromRow(row: QueryRowSet): Application? =
            try {
                Application(
                    row[ServicesList.service_id]!!,
                    row[ApplicationsList.contacts]!!,
                    row[ApplicationsList.education]!!,
                    row[ApplicationsList.certificates]!!,
                    row[ApplicationsList.workingExperience]!!,
                    row[SpecialistsList.specialist_id]!!,
                    row[ApplicationsList.status]!!,
                    row[ApplicationsList.comment]!!,
                    row[ApplicationsList.id]!!,
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
