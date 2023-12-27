package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.ONE
import ru.ac.uniyar.SIX

class GetPagesOfSpecialistsOperation(
    private val database: Database
) {
    fun getPagesAmount(
        initialsFilter: String,
        phoneNumberFilter: String
    ) = (
        FetchSpecialistsAmountOperation(database)
            .fetchSpecialistsAmount(initialsFilter, phoneNumberFilter) - ONE
        ) / SIX + ONE
}
