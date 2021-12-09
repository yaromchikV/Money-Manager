package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CategoriesRepository {

    fun getCategoryViewsForAccount(from: LocalDate, to: LocalDate, id: Int): Flow<List<CategoryView>>

    fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>>
}
