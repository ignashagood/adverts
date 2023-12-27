package ru.ac.uniyar.domain.db.essence

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.SpecialistsList
import java.lang.NullPointerException

data class Specialist(
    val locale: String,
    val initials: String,
    val education: String,
    val phoneNumber: String,
    val certificates: String,
    val workingExperience: String,
    val detailContacts: String,
    val login: String,
    val city: Int,
    val roleID: Int,
    val specialistId: Int = 0

) {
    companion object {
        fun fromRow(row: QueryRowSet): Specialist? =
            try {
                Specialist(
                    row[SpecialistsList.locale]!!,
                    row[SpecialistsList.initials]!!,
                    row[SpecialistsList.education]!!,
                    row[SpecialistsList.phoneNumber]!!,
                    row[SpecialistsList.certificates]!!,
                    row[SpecialistsList.workExperience]!!,
                    row[SpecialistsList.detailContacts]!!,
                    row[SpecialistsList.login]!!,
                    row[SpecialistsList.city]!!,
                    row[SpecialistsList.roleID]!!,
                    row[SpecialistsList.specialist_id]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
