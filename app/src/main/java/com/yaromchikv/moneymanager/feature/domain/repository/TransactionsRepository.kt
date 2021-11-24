package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionsRepository {

    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Int): Transaction?

    fun getTransactionAmountsPerDay(): Flow<List<DayInfo>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)
}