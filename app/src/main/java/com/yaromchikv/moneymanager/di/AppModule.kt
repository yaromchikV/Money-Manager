package com.yaromchikv.moneymanager.di

import android.app.Application
import androidx.room.Room
import com.yaromchikv.moneymanager.feature.data.datasource.MoneyManagerDatabase
import com.yaromchikv.moneymanager.feature.data.repository.AccountsRepositoryImpl
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountsUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.AddAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.DeleteAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.GetAccount
import com.yaromchikv.moneymanager.feature.domain.usecase.GetAccounts
import com.yaromchikv.moneymanager.feature.domain.usecase.UseCaseUpdateAccount
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAccountsDatabase(app: Application): MoneyManagerDatabase {
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
    fun provideAccountsUseCases(repository: AccountsRepository): AccountsUseCases {
        return AccountsUseCases(
            getAccounts = GetAccounts(repository),
            getAccount = GetAccount(repository),
            deleteAccount = DeleteAccount(repository),
            addAccount = AddAccount(repository),
            updateAccount = UseCaseUpdateAccount(repository)
        )
    }
}