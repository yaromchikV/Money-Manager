package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val color: Int
)