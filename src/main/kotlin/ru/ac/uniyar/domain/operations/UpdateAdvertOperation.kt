package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.essence.Advert
import ru.ac.uniyar.domain.db.tables.AdvertsList

class UpdateAdvertOperation(
    private val database: Database
) {
    fun update(
        advert: Advert
    ) {
        database.update(AdvertsList) {
            set(it.service_id, advert.serviceId)
            set(it.advert_name, advert.advertName)
            set(it.city_id, advert.cityId)
            set(it.description, advert.description)
            set(it.specialist_id, advert.specialistId)
            set(it.application_id, advert.applicationId)
            where { AdvertsList.advert_id eq advert.advertId }
        }
    }
}
