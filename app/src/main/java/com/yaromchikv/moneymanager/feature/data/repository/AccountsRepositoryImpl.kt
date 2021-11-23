package com.yaromchikv.moneymanager.feature.data.repository

import com.yaromchikv.moneymanager.feature.data.datasource.dao.AccountsDao
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow

class AccountsRepositoryImpl(
    private val dao: AccountsDao
) : AccountsRepository {

    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAccounts()
    }

    override suspend fun getAccountById(id: Int): Account? {
        return dao.getAccountById(id)
    }

    override suspend fun insertAccount(account: Account) {
        dao.insertAccount(account)
    }

    override suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }
}