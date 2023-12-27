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
import ru.ac.uniyar.domain.db.essence.Application
import ru.ac.uniyar.domain.db.tables.ApplicationsList
import ru.ac.uniyar.domain.db.tables.ServicesList
import ru.ac.uniyar.domain.db.tables.SpecialistsList

data class FetchApplicationsOperation(
    private val database: Database
) {
    fun fetchList(
        currentPage: Int,
        currentUserName: String,
        serviceName: String,
        status: String,
    ) =
        database
            .from(ApplicationsList)
            .leftJoin(SpecialistsList, ApplicationsList.specialistID eq SpecialistsList.specialist_id)
            .leftJoin(ServicesList, ApplicationsList.serviceID eq ServicesList.service_id)
            .select(
                ServicesList.service_id,
                ServicesList.service_name,
                ApplicationsList.contacts,
                ApplicationsList.education,
                ApplicationsList.certificates,
                ApplicationsList.workingExperience,
                ApplicationsList.status,
                ApplicationsList.comment,
                SpecialistsList.specialist_id,
                ApplicationsList.id
            )
            .where(
                (ServicesList.service_name like "%$serviceName%") and
                    (SpecialistsList.initials like "%$currentUserName%") and
                    (ApplicationsList.status like "%$status%")
            )
            .limit((currentPage - ONE) * SIX, SIX)
            .orderBy(ApplicationsList.id.desc())
            .mapNotNull(Application::fromRow)
}
