package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yaromchikv.moneymanager.common.TransactionType

@Entity
data class Transaction(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val note: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Int,
    val accountId: Int
)