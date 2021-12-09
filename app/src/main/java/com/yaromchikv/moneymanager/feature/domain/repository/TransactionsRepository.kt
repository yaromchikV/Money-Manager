package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionsRepository {

    fun getTransactionViews(from: LocalDate, to: LocalDate): Flow<List<TransactionView>>

    fun getTransactionViewsForAccount(from: LocalDate, to: LocalDate, id: Int): Flow<List<TransactionView>>

    fun getTransactionAmountsPerDay(from: LocalDate, to: LocalDate): Flow<List<DayInfo>>

    fun getTransactionAmountsPerDayForAccount(from: LocalDate, to: LocalDate, id: Int): Flow<List<DayInfo>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransactionById(id: Int)
}
