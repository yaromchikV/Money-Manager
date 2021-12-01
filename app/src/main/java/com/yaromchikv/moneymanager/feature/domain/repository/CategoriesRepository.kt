package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

//    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Int): Category?

    fun getCategoryViews(): Flow<List<CategoryView>>

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}