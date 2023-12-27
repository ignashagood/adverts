package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.count
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.groupBy
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.TEN
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.db.tables.AdvertsList
import ru.ac.uniyar.domain.db.tables.CitiesList
import ru.ac.uniyar.domain.db.tables.ServicesList

class FetchServicesInCityOperation(
    val database: Database
) {
    private val amountOfAdverts = count(AdvertsList.advert_id).aliased("amountOfAdverts")

    fun fetchList(
        cityId: Int,
    ) =
        database
            .from(AdvertsList)
            .leftJoin(ServicesList, AdvertsList.service_id eq ServicesList.service_id)
            .leftJoin(CitiesList, AdvertsList.city_id eq CitiesList.city_id)
            .select(
                ServicesList.service_name,
                ServicesList.service_id,
                ServicesList.locale,
                CitiesList.city_id,
                amountOfAdverts
            )
            .where(CitiesList.city_id eq cityId)
            .groupBy(AdvertsList.advert_id)
            .limit(TEN)
            .orderBy(AdvertsList.advert_id.desc())
            .mapNotNull {
                val service = Service.fromRow(it)
                val amountAdverts = it[amountOfAdverts]
                if (service == null || amountAdverts == null) null
                else Pair(service, amountAdverts)
            }
}
