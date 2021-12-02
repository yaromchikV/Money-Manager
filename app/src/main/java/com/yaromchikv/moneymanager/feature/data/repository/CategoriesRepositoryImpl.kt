package com.yaromchikv.moneymanager.feature.data.repository

import com.yaromchikv.moneymanager.feature.data.datasource.dao.CategoriesDao
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow

class CategoriesRepositoryImpl(
    private val dao: CategoriesDao
) : CategoriesRepository {

    override fun getCategoryViews(): Flow<List<CategoryView>> {
        return dao.getCategoryViews()
    }

}