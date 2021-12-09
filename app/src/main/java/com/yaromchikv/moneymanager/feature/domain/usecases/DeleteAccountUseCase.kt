package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository

class DeleteAccountUseCase(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.deleteAccount(account)
    }
}