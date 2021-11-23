package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Int): Category?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}