package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val icon: Int,
    @ColumnInfo(name = "icon_color") val iconColor: Int
)
