package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.ONE
import ru.ac.uniyar.SIX
import ru.ac.uniyar.domain.db.essence.Specialist
import ru.ac.uniyar.domain.db.tables.SpecialistsList

class FetchSpecialistsOperation(
    private val database: Database
) {
    fun fetchListWithFilters(
        currentPage: Int,
        initialsFilter: String,
        phoneNumberFilter: String
    ): List<Specialist> =
        database
            .from(SpecialistsList)
            .select()
            .where(
                (SpecialistsList.initials like "%$initialsFilter%") and
                    (SpecialistsList.phoneNumber like "%$phoneNumberFilter%")
            )
            .limit((currentPage - ONE) * SIX, SIX)
            /*В данном случае айди выдается автоматически при создании специалиста,
             и сортировка по specialist_id аналогична сортировке по дате добавления*/
            .orderBy(SpecialistsList.specialist_id.desc())
            .mapNotNull(Specialist::fromRow)
}
