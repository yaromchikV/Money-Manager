package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun addTransaction(transaction: Transaction) {
        transactionUseCases.addTransaction(transaction)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.AddTransaction)
        }
    }

    sealed class Event {
        object AddTransaction : Event()
    }

}