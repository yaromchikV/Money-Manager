package com.yaromchikv.moneymanager.feature.data.repository

import com.yaromchikv.moneymanager.feature.data.datasource.dao.CategoriesDao
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CategoriesRepositoryImpl(
    private val dao: CategoriesDao
) : CategoriesRepository {

    override fun getCategoryViewsForAccount(
        from: LocalDate,
        to: LocalDate,
        id: Int
    ): Flow<List<CategoryView>> {
        return dao.getCategoryViewsForAccount(from, to, id)
    }

    override fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>> {
        return dao.getCategoryViews(from, to)
    }
}
