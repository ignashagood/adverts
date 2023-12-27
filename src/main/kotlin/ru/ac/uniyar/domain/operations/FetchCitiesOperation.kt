package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.ONE
import ru.ac.uniyar.TWELVE
import ru.ac.uniyar.domain.db.essence.City
import ru.ac.uniyar.domain.db.tables.CitiesList

class FetchCitiesOperation(
    val database: Database
) {
    fun fetchListWithFilters(
        currentPage: Int,
        cityName: String
    ): List<City> =
        database
            .from(CitiesList)
            .select()
            .where(CitiesList.city_name like "%$cityName%")
            .limit((currentPage - ONE) * TWELVE, TWELVE)
            .mapNotNull(City::fromRow)
}
