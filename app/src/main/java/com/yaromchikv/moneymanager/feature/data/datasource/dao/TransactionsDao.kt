package com.yaromchikv.moneymanager.feature.data.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Query("SELECT * FROM 'transaction' ORDER BY 'datetime' ASC")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transaction' WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    @Query("SELECT date, SUM(amount) AS amount_per_day FROM 'transaction' GROUP BY date ORDER BY date DESC")
    fun getTransactionAmountsPerDay(): Flow<List<DayInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}