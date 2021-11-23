package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow

data class AccountsUseCases(
    val getAccounts: GetAccounts,
    val getAccount: GetAccount,
    val addAccount: AddAccount,
    val updateAccount: UpdateAccount,
    val deleteAccount: DeleteAccount,
)

class GetAccounts(private val repository: AccountsRepository) {
    operator fun invoke(): Flow<List<Account>> {
        return repository.getAccounts()
    }
}

class GetAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(id: Int): Account? {
        return repository.getAccountById(id)
    }
}

class AddAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.insertAccount(account)
    }
}

class UpdateAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.insertAccount(account)
    }
}

class DeleteAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.deleteAccount(account);
    }
}
