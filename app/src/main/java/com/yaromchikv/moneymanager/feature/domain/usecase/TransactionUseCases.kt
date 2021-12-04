package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.common.DateUtils.asLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.repository.AccountsRepository
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate

data class TransactionUseCases(
    val getTransactionViews: GetTransactionViews,
    val getTransactionListWithDayInfo: GetTransactionListWithAmountsPerDay,
    val addTransaction: AddTransaction,
    val deleteTransactionById: DeleteTransactionById
)

class GetTransactionViews(private val repository: TransactionsRepository) {
    operator fun invoke(from: LocalDate?, to: LocalDate?): Flow<List<TransactionView>> {
        val minDate: LocalDate = from ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = to ?: getCurrentLocalDate()

        return repository.getTransactionViews(minDate, maxDate)
    }
}

class GetTransactionListWithAmountsPerDay(private val repository: TransactionsRepository) {
    suspend operator fun invoke(
        transactions: List<TransactionView>,
        from: LocalDate?,
        to: LocalDate?
    ): List<Any> {
        val minDate: LocalDate = from ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = to ?: getCurrentLocalDate()

        val result = mutableListOf<Any>()
        val amountsPerDay = repository.getTransactionAmountsPerDay(minDate, maxDate).first()

        if (amountsPerDay.isNotEmpty()) {
            var i = 0
            for (it in transactions) {
                result.add(it)

                if (it.date != amountsPerDay[i].transactionDate) {
                    result.add(result.size - 1, amountsPerDay[i])
                    i++
                }
            }
            result.add(amountsPerDay[i])
        }
        return result.reversed()
    }
}

class AddTransaction(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        transactionsRepository.insertTransaction(transaction)
        accountsRepository.updateAccountAmountById(transaction.accountId, -1 * transaction.amount)
    }
}

class DeleteTransactionById(
    private val repository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: TransactionView) {
        repository.deleteTransactionById(transaction.id)
        accountsRepository.updateAccountAmountById(transaction.accountId, transaction.amount)
    }
}