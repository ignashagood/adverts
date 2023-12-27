package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class EditUserCityOperation(
    private val database: Database
) {
//    private val preparedSql = """
//        UPDATE ${SpecialistsList.tableName}
//        SET ${SpecialistsList.city.name} = ?
//        WHERE ${SpecialistsList.specialist_id} eq ?;
//    """.trimIndent()
    fun edit(
        newCityID: Int,
        userID: Int
    ) =
        database.update(SpecialistsList) {
            set(it.city, newCityID)
            where {
                it.specialist_id eq userID
            }
        }

//        database.useConnection { connection ->
//            connection.prepareStatement(preparedSql).use { statement ->
//                statement.setInt(1,newCityID)
//                statement.setInt(2, userID)
//
//            }
//        }
}
