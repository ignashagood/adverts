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
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.ONE
import ru.ac.uniyar.SIX
import ru.ac.uniyar.domain.db.essence.Advert
import ru.ac.uniyar.domain.db.tables.AdvertsList
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.CitiesList
import ru.ac.uniyar.domain.db.tables.ServicesList
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchAdvertsListOperation(
    private val database: Database
) {
    fun fetchListWithFilters(
        currentPage: Int,
        specialistNameFilter: String,
        cityNameFilter: String,
        serviceNameFilter: String
    ): List<Advert> =
        database
            .from(AdvertsList)
            .leftJoin(ServicesList, AdvertsList.service_id eq ServicesList.service_id)
            .leftJoin(CitiesList, AdvertsList.city_id eq CitiesList.city_id)
            .leftJoin(SpecialistsList, AdvertsList.specialist_id eq SpecialistsList.specialist_id)
            .leftJoin(ApplicationsList, AdvertsList.application_id eq ApplicationsList.id)
            .select(
                ServicesList.service_id,
                ServicesList.service_name,
                AdvertsList.advert_name,
                CitiesList.city_id,
                CitiesList.city_name,
                AdvertsList.description,
                SpecialistsList.specialist_id,
                SpecialistsList.initials,
                AdvertsList.advert_id,
                ApplicationsList.id,
                ApplicationsList.certificates,
                ApplicationsList.contacts,
                ApplicationsList.workingExperience,
                ApplicationsList.education
            )
            .where(
                (SpecialistsList.initials like "%$specialistNameFilter%") and
                    (CitiesList.city_name like "%$cityNameFilter%") and
                    (ServicesList.service_name like "%$serviceNameFilter%")
            )
            .limit((currentPage - ONE) * SIX, SIX)
                /*Как и в случае со специалистом, айди формируется автоматически,
                 поэтому фильтрация по дате добавления и айди аналогична*/
            .orderBy(AdvertsList.advert_id.desc())
            .mapNotNull(Advert::fromRow)
}
