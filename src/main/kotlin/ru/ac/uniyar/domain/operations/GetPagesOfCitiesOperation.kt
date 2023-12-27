package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.ONE
import ru.ac.uniyar.TWELVE

class GetPagesOfCitiesOperation(
    private val database: Database
) {
    fun getAmountOfPages(
        cityName: String = ""
    ) =
        (FetchCitiesListOperation(database).fetchList(cityName).size - ONE) / TWELVE + ONE
}
