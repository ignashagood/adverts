package ru.ac.uniyar.web.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery

class Paginator(
    private val uri: Uri,
    private val currentPage: Int,
    private val amountOfPages: Int
) {
    fun hasNextPage(): Boolean = currentPage < amountOfPages
    fun hasPrevPage(): Boolean = currentPage > 1

    private val uriValue = uri.removeQuery("page")
    val nextPageLink = uriValue.query("page", (currentPage + 1).toString())
    val prevPageLink = uriValue.query("page", (currentPage - 1).toString())
}
