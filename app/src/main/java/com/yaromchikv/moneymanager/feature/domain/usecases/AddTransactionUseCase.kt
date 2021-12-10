package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository

class AddTransactionUseCase(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        val account = accountsRepository.getAccountById(transaction.accountId)
        if (account != null) {
            val amount = account.amount - transaction.amount
            accountsRepository.updateAccountAmount(transaction.accountId, amount)
            transactionsRepository.insertTransaction(transaction)
        }
    }
}
