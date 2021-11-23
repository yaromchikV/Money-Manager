package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val icon: Int,
    val iconColor: Int
)
