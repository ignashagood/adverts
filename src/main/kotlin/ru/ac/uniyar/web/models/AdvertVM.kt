package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Advert

data class AdvertVM(
    val advert: Advert
) : ViewModel
