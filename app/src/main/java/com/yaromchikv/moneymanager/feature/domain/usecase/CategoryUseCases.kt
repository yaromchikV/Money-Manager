package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.common.asLocalDate
import com.yaromchikv.moneymanager.common.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class CategoryUseCases(
    val getCategoryViews: GetCategoryViews
)

class GetCategoryViews(private val repository: CategoriesRepository) {
    operator fun invoke(from: LocalDate?, to: LocalDate?): Flow<List<CategoryView>> {
        val minDate: LocalDate = from ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = to ?: getCurrentLocalDate()

        return repository.getCategoryViews(minDate, maxDate)
    }
}