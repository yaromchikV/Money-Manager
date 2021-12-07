package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.common.DateUtils.asLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class CategoryUseCases(
    val getCategoryViews: GetCategoryViews
)

class GetCategoryViews(private val repository: CategoriesRepository) {
    operator fun invoke(
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): Flow<List<CategoryView>> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()
        return if (account == null)
            repository.getCategoryViews(minDate, maxDate)
        else {
            repository.getCategoryViewsForAccount(minDate, maxDate, account.id ?: 0)
        }
    }
}