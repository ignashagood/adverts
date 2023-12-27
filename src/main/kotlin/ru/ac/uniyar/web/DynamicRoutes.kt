package ru.ac.uniyar.web

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.lens.RequestContextLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.operations.OperationHolder
import ru.ac.uniyar.web.filters.JwtTools
import ru.ac.uniyar.web.handlers.ShowLoginHandler
import ru.ac.uniyar.web.handlers.UserAutentification
import ru.ac.uniyar.web.handlers.UserLogout
import ru.ac.uniyar.web.handlers.createNewAdvert
import ru.ac.uniyar.web.handlers.createNewApplication
import ru.ac.uniyar.web.handlers.createNewSpecialist
import ru.ac.uniyar.web.handlers.editAdvert
import ru.ac.uniyar.web.handlers.editCity
import ru.ac.uniyar.web.handlers.makeDecision
import ru.ac.uniyar.web.handlers.makeRefuse
import ru.ac.uniyar.web.handlers.redirectToBeginning
import ru.ac.uniyar.web.handlers.refuseApplication
import ru.ac.uniyar.web.handlers.respondWithPong
import ru.ac.uniyar.web.handlers.showAdvert
import ru.ac.uniyar.web.handlers.showAdvertsList
import ru.ac.uniyar.web.handlers.showApplication
import ru.ac.uniyar.web.handlers.showApplicationsList
import ru.ac.uniyar.web.handlers.showChangeForm
import ru.ac.uniyar.web.handlers.showCitiesList
import ru.ac.uniyar.web.handlers.showCity
import ru.ac.uniyar.web.handlers.showNewAdvertForm
import ru.ac.uniyar.web.handlers.showNewApplicationForm
import ru.ac.uniyar.web.handlers.showNewSpecialistForm
import ru.ac.uniyar.web.handlers.showServicesList
import ru.ac.uniyar.web.handlers.showSpecialist
import ru.ac.uniyar.web.handlers.showSpecialistsList
import ru.ac.uniyar.web.handlers.showStartPage
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun app(
    htmlViewModel: ContextAwareViewRender,
    operationHolder: OperationHolder,
    currentUserLens: RequestContextLens<Specialist?>,
    permissionsLens: RequestContextLens<RoleRules>,
    jwtTools: JwtTools
): HttpHandler = routes(
    "/ping" bind Method.GET to respondWithPong(),
    "/" bind Method.GET to redirectToBeginning(),
    "/startPage" bind Method.GET to showStartPage(
        htmlViewModel,
        operationHolder.fetchSpecialistsAmountOperation,
        operationHolder.fetchAdvertsAmountOperation
    ),
    "/specialists" bind Method.GET to showSpecialistsList(
        htmlViewModel,
        operationHolder.getPagesOfSpecialistsOperation,
        operationHolder.fetchSpecialistsOperation,
        permissionsLens
    ),
    "/specialists/{number}" bind Method.GET to showSpecialist(
        htmlViewModel,
        operationHolder.fetchSpecialistOperation,
        operationHolder.getPagesOfAdvertsOperation,
        operationHolder.fetchAdvertsListOperation,
        operationHolder.fetchCityOperation,
        permissionsLens
    ),
    "/services" bind Method.GET to showServicesList(
        htmlViewModel,
        operationHolder.getPagesOfServicesAmountOperation,
        operationHolder.fetchServicesOperation,
        operationHolder.fetchServicesAmountOperation,
        operationHolder.fetchAdvertsAmountOperation,
        operationHolder.fetchServicesListOperation,
        operationHolder.fetchCitiesListOperation
    ),
    "/adverts" bind Method.GET to showAdvertsList(
        htmlViewModel,
        operationHolder.getPagesOfAdvertsOperation,
        operationHolder.fetchAdvertsListOperation,
        operationHolder.fetchServicesListOperation,
        operationHolder.fetchCitiesListOperation,
        operationHolder.fetchCityOperation,
        currentUserLens
    ),
    "/adverts/new" bind Method.GET to showNewAdvertForm(
        htmlViewModel,
        operationHolder.fetchSpecialistsServicesOperation,
        operationHolder.fetchCityOperation,
        permissionsLens,
        currentUserLens
    ),
    "/adverts/new" bind Method.POST to createNewAdvert(
        htmlViewModel,
        operationHolder.fetchServiceOperation,
        operationHolder.fetchCityOperation,
        operationHolder.fetchSpecialistsServicesOperation,
        operationHolder.addAdvertOperation,
        operationHolder.fetchApplicationByUserAndService,
        operationHolder.fetchServiceIDOperation,
        operationHolder.fetchAdvertsIDsOperation,
        operationHolder.updateAdvertOperation,
        currentUserLens
    ),
    "/adverts/{id}" bind Method.GET to showAdvert(
        htmlViewModel,
        operationHolder.fetchAdvertOperation,
        permissionsLens
    ),
    "/cities" bind Method.GET to showCitiesList(
        htmlViewModel,
        operationHolder.getPagesOfCitiesOperation,
        operationHolder.fetchCitiesOperation
    ),
    "/cities/{city_id}" bind Method.GET to showCity(
        htmlViewModel,
        operationHolder.fetchServicesInCityOperation,
        operationHolder.fetchCityOperation
    ),
    "/login" bind Method.GET to ShowLoginHandler(
        htmlViewModel
    ),
    "/login" bind Method.POST to UserAutentification(
        operationHolder.userLoginAuthentificationOperation,
        htmlViewModel,
        jwtTools
    ),
    "/registration" bind Method.GET to showNewSpecialistForm(
        htmlViewModel,
        operationHolder.fetchCitiesListOperation
    ),
    "registration" bind Method.POST to createNewSpecialist(
        htmlViewModel,
        operationHolder.addSpecialistOperation,
        operationHolder.fetchCitiesListOperation,
        operationHolder.fetchCityIDByCityNameOperation
    ),
    "/logout" bind Method.GET to UserLogout(),
    "/changeData" bind Method.GET to showChangeForm(
        htmlViewModel,
        operationHolder.fetchCitiesListOperation,
        permissionsLens
    ),
    "/changeData" bind Method.POST to editCity(
        operationHolder.fetchCityIDByCityNameOperation,
        currentUserLens,
        operationHolder.editUserCityOperation
    ),
    "/applications/new" bind Method.GET to showNewApplicationForm(
        htmlViewModel,
        operationHolder.fetchAvailableServicesOperation,
        permissionsLens,
        currentUserLens
    ),
    "/applications/new" bind Method.POST to createNewApplication(
        htmlViewModel,
        operationHolder.fetchAvailableServicesOperation,
        operationHolder.addApplicationOperation,
        operationHolder.fetchServiceIDOperation,
        currentUserLens
    ),
    "/applications" bind Method.GET to showApplicationsList(
        htmlViewModel,
        operationHolder.fetchServicesListOperation,
        operationHolder.fetchApplicationsOperation,
        operationHolder.fetchApplicationsAmountOperation,
        operationHolder.fetchServiceOperation,
        operationHolder.fetchSpecialistOperation,
        permissionsLens,
        currentUserLens
    ),
    "/applications/{id}" bind Method.GET to showApplication(
        htmlViewModel,
        operationHolder.fetchApplicationOperation,
        operationHolder.fetchServiceOperation,
        operationHolder.fetchSpecialistOperation,
        permissionsLens,
        currentUserLens,
    ),
    "/applications/{id}" bind Method.POST to makeDecision(
        operationHolder.fetchApplicationOperation,
        operationHolder.agreeApplicationOperation
    ),
    "/refuse/{id}" bind Method.GET to refuseApplication(
        htmlViewModel,
        permissionsLens
    ),
    "/refuse/{id}" bind Method.POST to makeRefuse(
        htmlViewModel,
        operationHolder.fetchApplicationOperation,
        operationHolder.refuseApplicationOperation
    ),
    "/edit/{id}" bind Method.GET to editAdvert(
        htmlViewModel,
        operationHolder.fetchSpecialistsServicesOperation,
        operationHolder.fetchAdvertOperation,
        operationHolder.fetchCityOperation,
        permissionsLens,
        currentUserLens
    ),
    "/edit/{id}" bind Method.POST to createNewAdvert(
        htmlViewModel,
        operationHolder.fetchServiceOperation,
//        operationHolder.fetchCityIDByCityNameOperation,
//        operationHolder.fetchAdvertOperation,
        operationHolder.fetchCityOperation,
        operationHolder.fetchSpecialistsServicesOperation,
        operationHolder.addAdvertOperation,
        operationHolder.fetchApplicationByUserAndService,
        operationHolder.fetchServiceIDOperation,
        operationHolder.fetchAdvertsIDsOperation,
        operationHolder.updateAdvertOperation,
        currentUserLens
    ),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)
