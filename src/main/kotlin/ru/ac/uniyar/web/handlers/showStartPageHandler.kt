package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.operations.FetchAdvertsAmountOperation
import ru.ac.uniyar.domain.operations.FetchSpecialistsAmountOperation
import ru.ac.uniyar.web.models.StartPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender
import kotlin.math.roundToInt

const val HUNDRED = 100.0

fun showStartPage(
    htmlViewModel: ContextAwareViewRender,
    fetchSpecialistsAmountOperation: FetchSpecialistsAmountOperation,
    fetchAdvertsAmountOperation: FetchAdvertsAmountOperation
): HttpHandler = {
    val specialistsAmount =
        fetchSpecialistsAmountOperation.fetchSpecialistsAmount()
    val advertsPerSpecialist =
        if (specialistsAmount == 0)
            0.0
        else
            fetchAdvertsAmountOperation.fetchAdvertsAmount() / specialistsAmount.toDouble()
    val beginningPage = StartPageVM(specialistsAmount, (advertsPerSpecialist * HUNDRED).roundToInt() / HUNDRED)
    Response(Status.OK).with(htmlViewModel(it) of beginningPage)
}
