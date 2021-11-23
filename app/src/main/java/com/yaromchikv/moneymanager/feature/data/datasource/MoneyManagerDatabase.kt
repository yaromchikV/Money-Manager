package com.yaromchikv.moneymanager.feature.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yaromchikv.moneymanager.feature.data.datasource.dao.AccountsDao
import com.yaromchikv.moneymanager.feature.data.datasource.dao.CategoriesDao
import com.yaromchikv.moneymanager.feature.data.datasource.dao.TransactionsDao
import com.yaromchikv.moneymanager.feature.domain.model.Account

@Database(entities = [Account::class], version = 1)
abstract class MoneyManagerDatabase : RoomDatabase() {

    abstract val accountsDao: AccountsDao
    abstract val categoriesDao: CategoriesDao
    abstract val transactionsDao: TransactionsDao

    companion object {
        const val DATABASE_NAME = "money_manager_db"
    }
}