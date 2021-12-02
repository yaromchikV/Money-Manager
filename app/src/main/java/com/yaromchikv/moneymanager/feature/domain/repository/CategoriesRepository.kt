package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CategoriesRepository {

    fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>>
}