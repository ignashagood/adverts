package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.ApplicationsList

data class RefuseApplicationOperation(
    private val database: Database
) {
    fun refuseApplication(
        applicationID: Int,
        comment: String
    ) =
        database
            .update(ApplicationsList) {
                set(ApplicationsList.status, "Отклонена")
                set(ApplicationsList.comment, comment)
                where {
                    it.id eq applicationID
                }
            }
}
