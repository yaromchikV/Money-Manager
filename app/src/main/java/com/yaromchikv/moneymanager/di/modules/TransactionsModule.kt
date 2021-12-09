package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.TransactionsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import com.yaromchikv.moneymanager.feature.domain.usecases.AddTransactionUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.DeleteTransactionByIdUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.GetTransactionViewsUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.GetTransactionsWithDayInfoUseCase
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
    fun provideTransactionsRepository(db: MoneyManagerDatabase): TransactionsRepository =
        TransactionsRepositoryImpl(db.transactionsDao)

    @Provides
    @Singleton
    fun provideGetTransactionViewsUseCase(transactionsRepository: TransactionsRepository): GetTransactionViewsUseCase =
        GetTransactionViewsUseCase(transactionsRepository)

    @Provides
    @Singleton
    fun provideGetTransactionListWithDayInfoUseCase(transactionsRepository: TransactionsRepository): GetTransactionsWithDayInfoUseCase =
        GetTransactionsWithDayInfoUseCase(transactionsRepository)

    @Provides
    @Singleton
    fun provideAddTransactionUseCase(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): AddTransactionUseCase = AddTransactionUseCase(transactionsRepository, accountsRepository)

    @Provides
    @Singleton
    fun provideDeleteTransactionByIdUseCase(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): DeleteTransactionByIdUseCase =
        DeleteTransactionByIdUseCase(transactionsRepository, accountsRepository)
}
