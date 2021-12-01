package com.yaromchikv.moneymanager.feature.data.repository

import com.yaromchikv.moneymanager.feature.data.datasource.dao.TransactionsDao
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TransactionsRepositoryImpl(
    private val dao: TransactionsDao
) : TransactionsRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions()
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return dao.getTransactionById(id)
    }

    override fun getTransactionAmountsPerDay(): Flow<List<DayInfo>> {
        return dao.getTransactionAmountsPerDay()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }
}