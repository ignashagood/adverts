package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.database.asIterable
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class UserLoginAuthentificationOperation(
    private val database: Database,
    private val salt: String
) {
    private val preparedSql = """
        select ${SpecialistsList.login.name} from ${SpecialistsList.tableName}
        where ${SpecialistsList.login.name} = ? and ${SpecialistsList.password.name} = HASH('SHA3-256', ?)
    """.trimIndent()

    companion object {
        class AuthentificationError : Exception()
    }

    fun authentificate(username: String, password: String) =
        database
            .useConnection { connection ->
                connection.prepareStatement(preparedSql).use { statement ->
                    statement.setString(1, username)
                    statement.setString(2, password + salt)
                    statement
                        .executeQuery()
                        .asIterable()
                        .map { row ->
                            row.getString(1)
                        }.firstOrNull() ?: throw AuthentificationError()
                }
            }
}
