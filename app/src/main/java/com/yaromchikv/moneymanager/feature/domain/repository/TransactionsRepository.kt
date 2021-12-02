package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactionViews(): Flow<List<TransactionView>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    fun getTransactionAmountsPerDay(): Flow<List<DayInfo>>
}