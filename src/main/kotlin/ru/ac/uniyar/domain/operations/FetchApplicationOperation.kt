package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Application
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.ServicesList
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchApplicationOperation(
    private val database: Database
) {
    fun fetch(applicationID: Int) =
        database
            .from(ApplicationsList)
            .leftJoin(ServicesList, ApplicationsList.serviceID eq ServicesList.service_id)
            .leftJoin(SpecialistsList, ApplicationsList.specialistID eq SpecialistsList.specialist_id)
            .select(
                ServicesList.service_id,
                ServicesList.service_name,
                ApplicationsList.contacts,
                ApplicationsList.education,
                ApplicationsList.certificates,
                ApplicationsList.workingExperience,
                SpecialistsList.specialist_id,
                ApplicationsList.status,
                ApplicationsList.comment,
                ApplicationsList.id
            )
            .where { ApplicationsList.id eq applicationID }
            .mapNotNull(Application::fromRow)
            .firstOrNull()
}
