package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.domain.db.essence.Service

class FetchAvailableServicesOperation(
    private val database: Database
) {
    fun fetchAvailableServices(
        userID: Int
    ): List<Service> {
        val allServicesList = FetchServicesListOperation(database).fetchList()
        val existingServicesList = FetchSpecialistsServicesOperation(database).fetchList(userID)
        val returnArray = mutableListOf<Service>()
        for (service in allServicesList)
            if (existingServicesList.map { el -> el.serviceId }.indexOf(service.serviceId) == -1)
                returnArray += service
        return returnArray
    }
}
