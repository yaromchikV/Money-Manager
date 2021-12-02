package com.yaromchikv.moneymanager.feature.data.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Query("SELECT categories.id, categories.name, categories.icon, categories.icon_color, IFNULL(SUM(transactions.amount), 0) AS category_amount FROM categories LEFT JOIN transactions ON transactions.category_id = categories.id GROUP BY categories.id")
    fun getCategoryViews(): Flow<List<CategoryView>>

}