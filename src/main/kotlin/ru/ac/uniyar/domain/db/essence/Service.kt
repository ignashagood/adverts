package ru.ac.uniyar.domain.db.essence

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.ServicesList
import java.lang.NullPointerException

data class Service(
    val locale: String,
    val serviceName: String,
    val serviceId: Int = 0
) {
    companion object {
        fun fromRow(row: QueryRowSet): Service? =
            try {
                Service(
                    row[ServicesList.locale]!!,
                    row[ServicesList.service_name]!!,
                    row[ServicesList.service_id]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
