package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.City
import ru.ac.uniyar.domain.db.tables.CitiesList

class FetchCityIDByCityNameOperation(
    private val database: Database
) {
    fun fetchID(cityName: String) =
        database
            .from(CitiesList)
            .select()
            .where { CitiesList.city_name like "%$cityName%" }
            .mapNotNull(City.Companion::fromRow)
            .firstOrNull()!!
            .cityId
}
