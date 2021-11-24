package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.model.TransactionType
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _transactions = MutableStateFlow(emptyList<Transaction>())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private var getTransactionsJob: Job? = null

    init {
        getTransactions()

//        viewModelScope.launch {
//            val tran1 = Transaction(
//                name = "tran1", amount = 13.5,
//                date = LocalDate.parse("2021-11-23"), time = LocalTime.parse("15:15:30"),
//                type = TransactionType.EXPENSE, categoryId = 1, accountId = 1
//            )
//            val tran2 = Transaction(
//                name = "tran2", amount = 131.25,
//                date = LocalDate.parse("2021-11-23"), time = LocalTime.parse("10:15:30"),
//                type = TransactionType.EXPENSE, categoryId = 1, accountId = 1
//            )
//            val tran3 = Transaction(
//                name = "tran3", amount = 2.3,
//                date = LocalDate.parse("2021-11-22"), time = LocalTime.parse("13:15:30"),
//                type = TransactionType.EXPENSE, categoryId = 1, accountId = 1
//            )
//            transactionUseCases.addTransaction(tran1)
//            transactionUseCases.addTransaction(tran2)
//            transactionUseCases.addTransaction(tran3)
//        }
    }

    private fun getTransactions() {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCases.getTransactions()
            .onEach { transactions -> _transactions.value = transactions }
            .launchIn(viewModelScope)
    }
}