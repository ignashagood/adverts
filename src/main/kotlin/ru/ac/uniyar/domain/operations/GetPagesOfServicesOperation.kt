package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.ONE
import ru.ac.uniyar.SIX

class GetPagesOfServicesOperation(
    private val database: Database
) {
    fun getPagesAmount(
        serviceNameFilter: String,
        cityNameFilter: String
    ) = (
        FetchServicesAmountOperation(database)
            .getServicesAmount(serviceNameFilter, cityNameFilter) - ONE
        ) / SIX + ONE
}
