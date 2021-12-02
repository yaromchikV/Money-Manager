package com.yaromchikv.moneymanager.feature.data.repository

import com.yaromchikv.moneymanager.feature.data.datasource.dao.TransactionsDao
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TransactionsRepositoryImpl(
    private val dao: TransactionsDao
) : TransactionsRepository {

    override fun getTransactionViews(from: LocalDate, to: LocalDate): Flow<List<TransactionView>> {
        return dao.getTransactionViews(from, to)
    }

    override fun getTransactionAmountsPerDay(from: LocalDate, to: LocalDate): Flow<List<DayInfo>> {
        return dao.getTransactionAmountsPerDay(from, to)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }
}