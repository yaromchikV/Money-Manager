package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.common.DateUtils
import com.yaromchikv.moneymanager.common.DateUtils.asLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetTransactionViewsUseCase(private val repository: TransactionsRepository) {
    operator fun invoke(
        dateRange: Pair<LocalDate?, LocalDate?>,
        account: Account?
    ): Flow<List<TransactionView>> {
        val minDate: LocalDate = dateRange.first ?: "2000-01-01".asLocalDate()
        val maxDate: LocalDate = dateRange.second ?: DateUtils.getCurrentLocalDate()
        return if (account == null)
            repository.getTransactionViews(minDate, maxDate)
        else {
            repository.getTransactionViewsForAccount(minDate, maxDate, account.id ?: 0)
        }
    }
}
