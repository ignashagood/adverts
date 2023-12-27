package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database

class OperationHolder(
    database: Database,
    salt: String
) {
    val addSpecialistOperation = AddSpecialistOperation(database, salt)
    val fetchSpecialistOperation = FetchSpecialistOperation(database)
    val fetchSpecialistsListOperation = FetchSpecialistsListOperation(database)
    val fetchServicesListOperation = FetchServicesListOperation(database)
    val fetchAdvertsListOperation = FetchAdvertsListOperation(database)
    val fetchServiceOperation = FetchServiceOperation(database)
    val fetchCityOperation = FetchCityOperation(database)
    val addAdvertOperation = AddAdvertOperation(database)
    val fetchCitiesOperation = FetchCitiesOperation(database)
    val fetchAdvertOperation = FetchAdvertOperation(database)
    val fetchServicesInCityOperation = FetchServicesInCityOperation(database)
    val getPagesOfAdvertsOperation = GetPagesOfAdvertsOperation(database)
    val fetchAdvertsAmountOperation = FetchAdvertsAmountOperation(database)
    val fetchServicesAmountOperation = FetchServicesAmountOperation(database)
    val getPagesOfServicesAmountOperation = GetPagesOfServicesOperation(database)
    val fetchCitiesListOperation = FetchCitiesListOperation(database)
    val getPagesOfCitiesOperation = GetPagesOfCitiesOperation(database)
    val fetchCityIDByCityNameOperation = FetchCityIDByCityNameOperation(database)
    val fetchServicesOperation = FetchServicesOperation(database)
    val fetchSpecialistIDOperation = FetchSpecialistIDOperation(database)
    val getPagesOfSpecialistsOperation = GetPagesOfSpecialistsOperation(database)
    val fetchSpecialistsAmountOperation = FetchSpecialistsAmountOperation(database)
    val fetchSpecialistsOperation = FetchSpecialistsOperation(database)
    val fetchUserViaUsernameOperation = FetchUserViaUsernameOperation(database)
    val fetchIsRoleValidOperation = FetchIsRoleValidOperation(database)
    val userLoginAuthentificationOperation = UserLoginAuthentificationOperation(database, salt)
    val editUserCityOperation = EditUserCityOperation(database)
    val fetchServiceIDOperation = FetchServiceIDOperation(database)
    val addApplicationOperation = AddApplicationOperation(database)
    val fetchApplicationsOperation = FetchApplicationsOperation(database)
    val fetchApplicationsAmountOperation = FetchApplicationsAmountOperation(database)
    val fetchApplicationOperation = FetchApplicationOperation(database)
    val agreeApplicationOperation = AgreeApplicationOperation(database)
    val refuseApplicationOperation = RefuseApplicationOperation(database)
    val fetchSpecialistsServicesOperation = FetchSpecialistsServicesOperation(database)
    val fetchAvailableServicesOperation = FetchAvailableServicesOperation(database)
    val fetchApplicationByUserAndService = FetchApplicationByUserAndService(database)
    val updateAdvertOperation = UpdateAdvertOperation(database)
    val fetchAdvertsIDsOperation = FetchAdvertsIDsOperation(database)
}
