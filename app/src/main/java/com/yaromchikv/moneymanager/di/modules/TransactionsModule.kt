package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.TransactionsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.AddTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransactionListForRV
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransactionViews
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.UpdateTransaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionsModule {

    @Provides
    @Singleton
    fun provideTransactionsRepository(db: MoneyManagerDatabase): TransactionsRepository {
        return TransactionsRepositoryImpl(db.transactionsDao)
    }

    @Provides
    @Singleton
    fun provideTransactionsUseCases(repository: TransactionsRepository): TransactionUseCases {
        return TransactionUseCases(
            // getTransactions = GetTransactions(repository),
            getTransaction = GetTransaction(repository),
            getTransactionViews = GetTransactionViews(repository),
            addTransaction = AddTransaction(repository),
            updateTransaction = UpdateTransaction(repository),
            deleteTransaction = DeleteTransaction(repository),
            getTransactionListForRV = GetTransactionListForRV(repository)
        )
    }
}