package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow

class GetAccountsUseCase(private val repository: AccountsRepository) {
    operator fun invoke(): Flow<List<Account>> {
        return repository.getAccounts()
    }
}