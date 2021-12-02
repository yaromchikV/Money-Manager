package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow

data class CategoryUseCases(
    val getCategoryViews: GetCategoryViews
)

class GetCategoryViews(private val repository: CategoriesRepository) {
    operator fun invoke(): Flow<List<CategoryView>> {
        return repository.getCategoryViews()
    }
}