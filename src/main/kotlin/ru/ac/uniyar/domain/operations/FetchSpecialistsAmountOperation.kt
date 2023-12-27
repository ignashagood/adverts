package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchSpecialistsAmountOperation(
    private val database: Database
) {
    fun fetchSpecialistsAmount(
        initialsFilter: String = "",
        phoneNumberFilter: String = ""
    ) =
        database
            .from(SpecialistsList)
            .select()
            .where(
                (SpecialistsList.initials like "%$initialsFilter%") and
                    (SpecialistsList.phoneNumber like "%$phoneNumberFilter%") and
                    (SpecialistsList.roleID eq 3)
            )
            .mapNotNull(Specialist::fromRow).count()
}
