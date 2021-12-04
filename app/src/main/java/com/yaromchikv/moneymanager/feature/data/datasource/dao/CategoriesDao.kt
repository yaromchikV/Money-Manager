package com.yaromchikv.moneymanager.feature.data.datasource.dao

import androidx.room.Dao
import androidx.room.Query
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CategoriesDao {

    @Query("SELECT id, name, icon, icon_color, ifnull((SELECT SUM(amount) FROM transactions WHERE categories.id = category_id AND date >= :from AND date <= :to), 0) AS category_amount FROM categories GROUP BY id")
    fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>>

}