package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository

class UpdateAccountUseCase(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.updateAccount(account)
    }
}
