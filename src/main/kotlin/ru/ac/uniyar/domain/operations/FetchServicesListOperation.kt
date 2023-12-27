package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.essence.Service
import ru.ac.uniyar.domain.db.tables.ServicesList

class FetchServicesListOperation(
    private val database: Database
) {
    fun fetchList(): List<Service> =
        database
            .from(ServicesList)
            .select()
            .mapNotNull(Service::fromRow)
}
