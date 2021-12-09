package com.yaromchikv.moneymanager.di.modules

import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.AccountsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.usecases.AddAccountUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.DeleteAccountUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.GetAccountsUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.UpdateAccountUseCase
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
    fun provideAccountsRepository(db: MoneyManagerDatabase): AccountsRepository =
        AccountsRepositoryImpl(db.accountsDao)

    @Provides
    @Singleton
    fun provideGetAccountsUseCase(repository: AccountsRepository): GetAccountsUseCase =
        GetAccountsUseCase(repository)

    @Provides
    @Singleton
    fun provideAddAccountUseCase(repository: AccountsRepository): AddAccountUseCase =
        AddAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateAccountUseCase(repository: AccountsRepository): UpdateAccountUseCase =
        UpdateAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(repository: AccountsRepository): DeleteAccountUseCase =
        DeleteAccountUseCase(repository)
}
