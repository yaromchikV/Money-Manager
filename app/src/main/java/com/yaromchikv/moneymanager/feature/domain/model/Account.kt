package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yaromchikv.moneymanager.common.listOfColors

@Entity
data class Account(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val color: Int
)