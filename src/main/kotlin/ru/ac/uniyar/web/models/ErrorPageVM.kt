package ru.ac.uniyar.web.models

import org.http4k.core.Uri
import org.http4k.template.ViewModel

data class ErrorPageVM(
    val uri: Uri
) : ViewModel
