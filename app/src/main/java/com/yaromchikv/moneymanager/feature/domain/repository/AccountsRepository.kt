package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    fun getAccounts(): Flow<List<Account>>

    suspend fun getAccountById(id: Int): Account?

    suspend fun insertAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun updateAccountAmount(id: Int, amount: Double)

    suspend fun deleteAccount(account: Account)
}
