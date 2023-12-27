package ru.ac.uniyar.domain.db.essence

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.CitiesList
import java.lang.NullPointerException

data class City(
    val cityName: String,
    val cityId: Int = 0
) {
    companion object {
        fun fromRow(row: QueryRowSet): City? =
            try {
                City(
                    row[CitiesList.city_name]!!,
                    row[CitiesList.city_id]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
