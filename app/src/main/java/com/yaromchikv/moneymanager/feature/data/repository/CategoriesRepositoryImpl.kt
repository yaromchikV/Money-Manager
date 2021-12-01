package com.yaromchikv.moneymanager.feature.data.repository

import com.yaromchikv.moneymanager.feature.data.datasource.dao.CategoriesDao
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.CategoryWithAmount
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow

class CategoriesRepositoryImpl(
    private val dao: CategoriesDao
) : CategoriesRepository {

    override fun getCategories(): Flow<List<Category>> {
        return dao.getCategories()
    }

    override suspend fun getCategoryById(id: Int): Category? {
        return dao.getCategoryById(id)
    }

    override fun getCategoriesWithAmount(): Flow<List<CategoryWithAmount>> {
        return dao.getCategoriesWithAmount()
    }

    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        dao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category)
    }
}