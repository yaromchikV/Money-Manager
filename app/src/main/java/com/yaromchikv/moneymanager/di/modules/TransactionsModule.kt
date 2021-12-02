package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.TransactionsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.AddTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteTransaction
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransactionListForRV
import com.yaromchikv.moneymanager.feature.domain.usecase.GetTransactionViews
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
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
    fun provideTransactionsUseCases(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): TransactionUseCases {
        return TransactionUseCases(
            getTransactionViews = GetTransactionViews(transactionsRepository),
            addTransaction = AddTransaction(transactionsRepository, accountsRepository),
            deleteTransaction = DeleteTransaction(transactionsRepository),
            getTransactionListForRV = GetTransactionListForRV(transactionsRepository)
        )
    }
}