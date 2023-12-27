package ru.ac.uniyar.domain.db.roles

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.RoleTable

data class RoleRules(
    val id: Int,
    val name: String,
    val advertPage: Boolean = false,
    val changingCity: Boolean = false,
    val sendingApplication: Boolean = false,
    val applicationsList: Boolean = false,
    val applicationsPage: Boolean = false,
    val agreement: Boolean = false,
    val appendAdvert: Boolean = false,
    val editAdvert: Boolean = false
) {
    companion object {
        val ANONYMOUS_ROLE = RoleRules(
            id = 0,
            name = "Анонимный",
        )

        fun fromRow(row: QueryRowSet): RoleRules? =
            try {
                RoleRules(
                    row[RoleTable.id]!!,
                    row[RoleTable.name]!!,
                    row[RoleTable.advertPage]!!,
                    row[RoleTable.changingCity]!!,
                    row[RoleTable.sendingApplication]!!,
                    row[RoleTable.applicationsList]!!,
                    row[RoleTable.applicationsPage]!!,
                    row[RoleTable.agreement]!!,
                    row[RoleTable.appendAdvert]!!,
                    row[RoleTable.editAdvert]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
