package com.yaromchikv.moneymanager.feature.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionsDao {

    @Query("SELECT transactions.id, transactions.note, transactions.amount, transactions.date, transactions.time, categories.id AS category_id, categories.name AS category_name, accounts.id AS account_id, accounts.name AS account_name, categories.icon, categories.icon_color FROM transactions JOIN accounts ON accounts.id = transactions.account_id JOIN categories ON categories.id = transactions.category_id WHERE date >= :from AND date <= :to ORDER BY date ASC, time ASC")
    fun getTransactionViews(from: LocalDate, to: LocalDate): Flow<List<TransactionView>>

    @Query("SELECT date, SUM(amount) AS amount_per_day FROM transactions WHERE date >= :from AND date <= :to GROUP BY date ORDER BY date ASC")
    fun getTransactionAmountsPerDay(from: LocalDate, to: LocalDate): Flow<List<DayInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)
}