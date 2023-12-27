package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.selectDistinct
import org.ktorm.dsl.where
import ru.ac.uniyar.ONE
import ru.ac.uniyar.SIX
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.db.tables.AdvertsList
import ru.ac.uniyar.domain.db.tables.CitiesList
import ru.ac.uniyar.domain.db.tables.ServicesList

class FetchServicesOperation(
    private val database: Database
) {
    fun fetchListWithFilters(
        currentPage: Int,
        serviceNameFilter: String,
        cityNameFilter: String
    ): List<Service> =
        database
            .from(AdvertsList)
            .leftJoin(ServicesList, AdvertsList.service_id eq ServicesList.service_id)
            .leftJoin(CitiesList, AdvertsList.city_id eq CitiesList.city_id)
            .selectDistinct(
                ServicesList.service_id,
                ServicesList.service_name,
                ServicesList.locale,
                CitiesList.city_name,
            )
            .where(
                (ServicesList.service_name like "%$serviceNameFilter%") and
                    (CitiesList.city_name like "%$cityNameFilter%")
            )
            .limit((currentPage - ONE) * SIX, SIX)
            .orderBy(ServicesList.locale.desc())
            .mapNotNull(Service::fromRow).distinct()
}
