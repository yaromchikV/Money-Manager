package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.DeleteAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountActionsViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun editButtonClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.NavigateToEditAccountScreen(account))
        }
    }

    fun deleteButtonClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.ShowTheDeleteAccountDialog(account))
        }
    }

    fun deleteConfirmationButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.DeleteAccount)
        }
    }

    suspend fun deleteAccount(account: Account) {
        deleteAccountUseCase(account)
    }

    sealed class Event {
        data class NavigateToEditAccountScreen(val account: Account) : Event()
        data class ShowTheDeleteAccountDialog(val account: Account) : Event()
        object DeleteAccount : Event()
    }
}
