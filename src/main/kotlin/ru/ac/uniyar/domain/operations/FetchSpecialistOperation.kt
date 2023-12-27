package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchSpecialistOperation(
    private val database: Database
) {
    fun fetch(specialistID: Int) =
        database
            .from(SpecialistsList)
            .select()
            .where { SpecialistsList.specialist_id eq specialistID }
            .mapNotNull(Specialist::fromRow)
            .firstOrNull()
}
