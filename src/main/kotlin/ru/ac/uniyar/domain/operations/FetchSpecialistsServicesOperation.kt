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
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.ServicesList
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchSpecialistsServicesOperation(
    private val database: Database
) {
    fun fetchList(
        currentUserID: Int
    ) =
        database
            .from(ApplicationsList)
            .leftJoin(SpecialistsList, ApplicationsList.specialistID eq SpecialistsList.specialist_id)
            .leftJoin(ServicesList, ApplicationsList.serviceID eq ServicesList.service_id)
            .select(
                ServicesList.service_name,
                ServicesList.service_id,
                ServicesList.locale,
                ApplicationsList.status,
                SpecialistsList.specialist_id,

            )
            .where(
                (SpecialistsList.specialist_id eq currentUserID) and
                    (ApplicationsList.status like "Подтверждена")
            )
            .mapNotNull(Service::fromRow)
}
