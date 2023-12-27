package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchSpecialistIDOperation(
    private val database: Database
) {
    fun fetchID(specialistName: String) =
        database
            .from(SpecialistsList)
            .select()
            .where { SpecialistsList.initials like "%$specialistName%" }
            .mapNotNull(Specialist::fromRow)
            .firstOrNull()!!
            .specialistId
}
