package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.common.DateUtils
import com.yaromchikv.moneymanager.common.DateUtils.asLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.first

class GetTransactionsWithDayInfoUseCase(private val repository: TransactionsRepository) {
    suspend operator fun invoke(
        transactions: List<TransactionView>,
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): List<Any> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: DateUtils.getCurrentLocalDate()

        val amountsPerDay = if (account == null)
            repository.getTransactionAmountsPerDay(minDate, maxDate).first()
        else {
            repository.getTransactionAmountsPerDayForAccount(minDate, maxDate, account.id ?: 0)
                .first()
        }

        val result = mutableListOf<Any>()

        if (amountsPerDay.isNotEmpty()) {
            var i = 0
            for (it in transactions) {
                result.add(it)
                if (it.date != amountsPerDay[i].transactionDate) {
                    result.add(result.size - 1, amountsPerDay[i])
                    i++
                }
            }
            if (i < amountsPerDay.size)
                result.add(amountsPerDay[i])
        }
        return result.reversed()
    }
}
