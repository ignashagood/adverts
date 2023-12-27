package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchUserViaUsernameOperation(
    private val database: Database
) {
    fun fetch(login: String): Specialist? =
        database
            .from(SpecialistsList)
            .select()
            .where { SpecialistsList.login like "%$login%" }
            .mapNotNull(Specialist::fromRow)
            .firstOrNull()
}
