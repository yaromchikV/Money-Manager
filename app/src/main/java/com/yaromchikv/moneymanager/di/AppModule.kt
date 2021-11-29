package com.yaromchikv.moneymanager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.AccountsRepositoryImpl
import com.yaromchikv.moneymanager.feature.data.repository.CategoriesRepositoryImpl
import com.yaromchikv.moneymanager.feature.data.repository.TransactionsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.CategoriesRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.AddAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.AddCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.AddTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.GetAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.GetAccounts
import com.yaromchikv.moneymanager.feature.domain.usecase.GetCategories
import com.yaromchikv.moneymanager.feature.domain.usecase.GetCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransactionListForRV
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransactions
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.UpdateAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.UpdateCategory
import com.yaromchikv.moneymanager.feature.domain.usecase.UpdateTransaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MoneyManagerDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            MoneyManagerDatabase::class.java,
            MoneyManagerDatabase.DATABASE_NAME
        ).createFromAsset("database/money_manager.db").build()
    }
}