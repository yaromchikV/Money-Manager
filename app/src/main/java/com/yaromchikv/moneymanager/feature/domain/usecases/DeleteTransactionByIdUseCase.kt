package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository

class DeleteTransactionByIdUseCase(
    private val repository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: TransactionView) {
        val account = accountsRepository.getAccountById(transaction.accountId)
        if (account != null) {
            val amount = account.amount + transaction.amount
            accountsRepository.updateAccountAmount(transaction.accountId, amount)

            repository.deleteTransactionById(transaction.id)
        }
    }
}