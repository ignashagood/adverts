package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel

data class StartPageVM(
    val specialistsAmount: Int,
    val avgAdverts: Double
) : ViewModel
