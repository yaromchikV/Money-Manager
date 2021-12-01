package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class CategoryWithAmount(
    val id: Int,
    val name: String,
    val icon: Int,
    @ColumnInfo(name = "icon_color")
    val iconColor: Int,
    @ColumnInfo(name = "category_amount")
    val amount: Double
)
