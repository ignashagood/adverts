package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.essence.Application

data class ApplicationVM(
    val application: Application,
    val serviceName: String,
    val userName: String
) : ViewModel
