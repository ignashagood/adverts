package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.domain.db.tables.SpecialistsList
import java.sql.Timestamp

class AddSpecialistOperation(
    private val database: Database,
    private val salt: String
) {
    private val preparedSql = """
        INSERT INTO $SpecialistsList
        (
        ${SpecialistsList.locale.name},
        ${SpecialistsList.initials.name},
        ${SpecialistsList.education.name},
        ${SpecialistsList.phoneNumber.name},
        ${SpecialistsList.certificates.name},
        ${SpecialistsList.workExperience.name},
        ${SpecialistsList.detailContacts.name},
        ${SpecialistsList.login.name},
        ${SpecialistsList.password.name},
        ${SpecialistsList.city.name},
        ${SpecialistsList.roleID.name}
         )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, HASH('SHA3-256', ?), ?, ?)
    """.trimIndent()

    fun addSpecialist(
        initials: String,
        education: String,
        phoneNumber: String,
        certificates: String,
        workExperience: String,
        contactDetails: String,
        login: String,
        password: String,
        city: Int
    ) {
        database.useConnection { connection ->
            connection.prepareStatement(preparedSql).use { statement ->
                statement.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                statement.setString(2, initials)
                statement.setString(3, education)
                statement.setString(4, phoneNumber)
                statement.setString(5, certificates)
                statement.setString(6, workExperience)
                statement.setString(7, contactDetails)
                statement.setString(8, login)
                statement.setString(9, password + salt)
                statement.setInt(10, city)
                statement.setInt(11, 1)
                statement.executeUpdate()
            }
        }
    }
}
