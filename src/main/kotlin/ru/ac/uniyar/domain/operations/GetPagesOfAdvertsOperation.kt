package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.ONE
import ru.ac.uniyar.SIX

class GetPagesOfAdvertsOperation(
    private val database: Database
) {
    fun getPagesAmount(
        specialistNameFilter: String,
        cityNameFilter: String,
        serviceNameFilter: String
    ) = (
        FetchAdvertsAmountOperation(database)
            .fetchAdvertsAmount(
                specialistNameFilter, cityNameFilter, serviceNameFilter
            ) - ONE
        ) / SIX + ONE
}
