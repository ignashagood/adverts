package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.roles.RoleRules
import ru.ac.uniyar.domain.db.tables.RoleTable

class FetchIsRoleValidOperation(
    val database: Database
) {
    fun fetch(roleId: Int): RoleRules? {
        return database
            .from(RoleTable)
            .select()
            .where { RoleTable.id eq roleId }
            .mapNotNull(RoleRules::fromRow)
            .firstOrNull()
    }
}
