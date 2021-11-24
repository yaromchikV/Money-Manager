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
        ).build()
    }

    @Provides
    @Singleton
    fun provideAccountsRepository(db: MoneyManagerDatabase): AccountsRepository {
        return AccountsRepositoryImpl(db.accountsDao)
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(db: MoneyManagerDatabase): CategoriesRepository {
        return CategoriesRepositoryImpl(db.categoriesDao)
    }

    @Provides
    @Singleton
    fun provideTransactionsRepository(db: MoneyManagerDatabase): TransactionsRepository {
        return TransactionsRepositoryImpl(db.transactionsDao)
    }

    @Provides
    @Singleton
    fun provideAccountsUseCases(repository: AccountsRepository): AccountUseCases {
        return AccountUseCases(
            getAccounts = GetAccounts(repository),
            getAccount = GetAccount(repository),
            addAccount = AddAccount(repository),
            updateAccount = UpdateAccount(repository),
            deleteAccount = DeleteAccount(repository),
        )
    }

    @Provides
    @Singleton
    fun provideCategoriesUseCases(repository: CategoriesRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategories = GetCategories(repository),
            getCategory = GetCategory(repository),
            addCategory = AddCategory(repository),
            updateCategory = UpdateCategory(repository),
            deleteCategory = DeleteCategory(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTransactionsUseCases(repository: TransactionsRepository): TransactionUseCases {
        return TransactionUseCases(
            getTransactions = GetTransactions(repository),
            getTransaction = GetTransaction(repository),
            addTransaction = AddTransaction(repository),
            updateTransaction = UpdateTransaction(repository),
            deleteTransaction = DeleteTransaction(repository),
            getTransactionListForRV = GetTransactionListForRV(repository)
        )
    }
}