package ru.ac.uniyar.domain.db.essence

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.AdvertsList
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.CitiesList
import ru.ac.uniyar.domain.db.tables.ServicesList
import ru.ac.uniyar.domain.db.tables.SpecialistsList
import java.lang.NullPointerException

data class Advert(
    val serviceId: Int,
    val serviceName: String,
    val advertName: String,
    val cityId: Int,
    val cityName: String,
    val description: String,
    val specialistId: Int,
    val specialistInitials: String,
    val contacts: String,
    val education: String,
    val certificates: String,
    val workingExperience: String,
    val applicationId: Int,
    var advertId: Int = 0
) {
    companion object {
        fun fromRow(row: QueryRowSet): Advert? =
            try {
                Advert(
                    row[ServicesList.service_id]!!,
                    row[ServicesList.service_name]!!,
                    row[AdvertsList.advert_name]!!,
                    row[CitiesList.city_id]!!,
                    row[CitiesList.city_name]!!,
                    row[AdvertsList.description]!!,
                    row[SpecialistsList.specialist_id]!!,
                    row[SpecialistsList.initials]!!,
                    row[ApplicationsList.contacts]!!,
                    row[ApplicationsList.education]!!,
                    row[ApplicationsList.certificates]!!,
                    row[ApplicationsList.workingExperience]!!,
                    row[ApplicationsList.id]!!,
                    row[AdvertsList.advert_id]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
