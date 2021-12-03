package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _transactionsWithDayInfo = MutableStateFlow(emptyList<Any>())
    val transactionsWithDayInfo: StateFlow<List<Any>> = _transactionsWithDayInfo

    private var getTransactionsJob: Job? = null

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getTransactions()
    }

    private fun getTransactions(from: LocalDate? = null, to: LocalDate? = null) {
        getTransactionsJob?.cancel()
        getTransactionsJob =
            transactionUseCases.getTransactionViews(from, to)
                .onEach { transactions ->
                    _transactionsWithDayInfo.value =
                        transactionUseCases.getTransactionListWithDayInfo(transactions, from, to)
                }
                .launchIn(viewModelScope)
    }

    fun setDateRange(from: LocalDate?, to: LocalDate?) {
        getTransactions(from, to)
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

    fun deleteButtonClick(transaction: TransactionView) {
        viewModelScope.launch {
            _events.emit(Event.ShowTheDeleteTransactionDialog(transaction))
        }
    }

    fun deleteConfirmationButtonClick(transaction: TransactionView) {
        viewModelScope.launch {
            _events.emit(Event.DeleteTransaction(transaction))
        }
    }

    suspend fun deleteTransaction(transaction: TransactionView) {
        transactionUseCases.deleteTransactionById(transaction)
    }

    sealed class Event {
        object SelectDate : Event()
        data class OpenTheAddTransactionSheet(val account: Account) : Event()
        data class ShowTheDeleteTransactionDialog(val transaction: TransactionView): Event()
        data class DeleteTransaction(val transaction: TransactionView): Event()
    }
}