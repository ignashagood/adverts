package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.db.tables.AdvertsList
import ru.ac.uniyar.domain.db.tables.CitiesList
import ru.ac.uniyar.domain.db.tables.ServicesList

class FetchServicesAmountOperation(
    private val database: Database
) {
    fun getServicesAmount(
        serviceNameFilter: String,
        cityNameFilter: String
    ) =
        database
            .from(AdvertsList)
            .leftJoin(ServicesList, AdvertsList.service_id eq ServicesList.service_id)
            .leftJoin(CitiesList, AdvertsList.city_id eq CitiesList.city_id)
            .select(
                ServicesList.service_id,
                ServicesList.service_name,
                ServicesList.locale,
                CitiesList.city_name,
            )
            .where(
                (ServicesList.service_name like "%$serviceNameFilter%") and
                    (CitiesList.city_name like "%$cityNameFilter%")
            )
            .mapNotNull(Service::fromRow).distinct().count()
}
