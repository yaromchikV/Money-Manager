package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.AccountsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.AddAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.GetAccounts
import com.yaromchikv.moneymanager.feature.domain.usecase.UpdateAccount
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountsModule {

    @Provides
    @Singleton
    fun provideAccountsRepository(db: MoneyManagerDatabase): AccountsRepository {
        return AccountsRepositoryImpl(db.accountsDao)
    }

    @Provides
    @Singleton
    fun provideAccountsUseCases(repository: AccountsRepository): AccountUseCases {
        return AccountUseCases(
            getAccounts = GetAccounts(repository),
            addAccount = AddAccount(repository),
            updateAccount = UpdateAccount(repository),
            deleteAccount = DeleteAccount(repository),
        )
    }
}