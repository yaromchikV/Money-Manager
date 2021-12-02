package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

data class TransactionUseCases(
    val getTransactionViews: GetTransactionViews,
    val addTransaction: AddTransaction,
    val deleteTransaction: DeleteTransaction,
    val getTransactionListForRV: GetTransactionListForRV
)

class GetTransactionViews(private val repository: TransactionsRepository) {
    operator fun invoke(): Flow<List<TransactionView>> {
        return repository.getTransactionViews()
    }
}

class AddTransaction(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        transactionsRepository.insertTransaction(transaction)
        accountsRepository.updateAccountAmountById(transaction.accountId, transaction.amount)
    }
}

class DeleteTransaction(private val repository: TransactionsRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}

class GetTransactionListForRV(private val repository: TransactionsRepository) {
    suspend operator fun invoke(transactions: List<TransactionView>): List<Any> {
        val result = mutableListOf<Any>()

        val amountsPerDay = repository.getTransactionAmountsPerDay().first()

        var i = 0
        for (it in transactions) {
            result.add(it)

            if (it.date != amountsPerDay[i].transactionDate) {
                result.add(result.size - 1, amountsPerDay[i])
                i++
            }
        }
        result.add(amountsPerDay[i])

        return result.reversed()
    }
}