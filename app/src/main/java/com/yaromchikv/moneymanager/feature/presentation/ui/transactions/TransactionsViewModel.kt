package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _transactions = MutableStateFlow(emptyList<Transaction>())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private var getTransactionsJob: Job? = null

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getTransactions()
    }

    private fun getTransactions() {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCases.getTransactions()
            .onEach { transactions -> _transactions.value = transactions }
            .launchIn(viewModelScope)
    }

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    fun addTransactionClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.OpenTheAddTransactionSheet(account))
        }
    }

    sealed class Event {
        object SelectDate: Event()
        data class OpenTheAddTransactionSheet(val account: Account) : Event()
    }
}