package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.db.tables.ServicesList

class FetchServiceIDOperation(
    private val database: Database
) {
    fun fetchID(serviceName: String): Int =
        database
            .from(ServicesList)
            .select()
            .where(ServicesList.service_name like "%$serviceName%")
            .mapNotNull(Service::fromRow)
            .firstOrNull()?.serviceId!!
}
