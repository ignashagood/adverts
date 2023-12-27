package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.support.mysql.insertOrUpdate
import ru.ac.uniyar.domain.db.essence.Advert
import ru.ac.uniyar.domain.db.tables.AdvertsList

class AddAdvertOperation(
    private val database: Database
) {
    fun addAdvert(advert: Advert) =
        database.insertOrUpdate(AdvertsList) {
            set(it.service_id, advert.serviceId)
            set(it.advert_name, advert.advertName)
            set(it.city_id, advert.cityId)
            set(it.description, advert.description)
            set(it.specialist_id, advert.specialistId)
            set(it.application_id, advert.applicationId)
        }
}
