package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchSpecialistsListOperation(
    private val database: Database
) {
    fun fetchList(
        initials: String = ""
    ): List<Specialist> =
        database
            .from(SpecialistsList)
            .select()
            .where(SpecialistsList.initials like "%$initials%")
            .mapNotNull(Specialist::fromRow)
}
