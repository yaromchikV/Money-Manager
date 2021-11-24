package com.yaromchikv.moneymanager.feature.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yaromchikv.moneymanager.feature.data.datasource.dao.AccountsDao
import com.yaromchikv.moneymanager.feature.data.datasource.dao.CategoriesDao
import com.yaromchikv.moneymanager.feature.data.datasource.dao.TransactionsDao
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.Transaction

@Database(
    entities = [Account::class, Category::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class MoneyManagerDatabase : RoomDatabase() {

    abstract val accountsDao: AccountsDao
    abstract val categoriesDao: CategoriesDao
    abstract val transactionsDao: TransactionsDao

    companion object {
        const val DATABASE_NAME = "money_manager_db"
    }
}