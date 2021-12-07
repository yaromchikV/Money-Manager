package com.yaromchikv.moneymanager.feature.domain.usecase

import com.yaromchikv.moneymanager.common.DateUtils.asLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Account
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
    operator fun invoke(
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): Flow<List<TransactionView>> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()
        return if (account == null)
            repository.getTransactionViews(minDate, maxDate)
        else {
            repository.getTransactionViewsForAccount(minDate, maxDate, account.id ?: 0)
        }
    }
}

class GetTransactionListWithAmountsPerDay(private val repository: TransactionsRepository) {
    suspend operator fun invoke(
        transactions: List<TransactionView>,
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): List<Any> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: getCurrentLocalDate()

        val result = mutableListOf<Any>()

        val amountsPerDay = if (account == null)
            repository.getTransactionAmountsPerDay(minDate, maxDate).first()
        else {
            repository.getTransactionAmountsPerDayForAccount(minDate, maxDate, account.id ?: 0).first()
        }

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
        val account = accountsRepository.getAccountById(transaction.accountId)
        if (account != null) {
            val amount = transaction.amount + account.amount
            accountsRepository.updateAccountAmount(transaction.accountId, amount)

            transactionsRepository.insertTransaction(transaction)
        }
    }
}

class DeleteTransactionById(
    private val repository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(transaction: TransactionView) {
        val account = accountsRepository.getAccountById(transaction.accountId)
        if (account != null) {
            val amount = account.amount - transaction.amount
            accountsRepository.updateAccountAmount(transaction.accountId, amount)

            repository.deleteTransactionById(transaction.id)
        }
    }
}