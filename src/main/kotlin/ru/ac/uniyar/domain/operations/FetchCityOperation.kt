package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.City
import ru.ac.uniyar.domain.db.tables.CitiesList

class FetchCityOperation(
    private val database: Database
) {
    fun fetch(cityID: Int) =
        database
            .from(CitiesList)
            .select()
            .where { CitiesList.city_id eq cityID }
            .mapNotNull(City.Companion::fromRow)
            .firstOrNull()
}
