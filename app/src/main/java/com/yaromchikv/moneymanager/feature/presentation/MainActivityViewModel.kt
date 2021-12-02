package com.yaromchikv.moneymanager.feature.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val accountUseCases: AccountUseCases
) : ViewModel() {

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts = _accounts.asStateFlow()

    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount = _currentAccount.asStateFlow()

    private val _currentDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(
        getCurrentLocalDate() to getCurrentLocalDate()
    )
    val currentDateRange = _currentDateRange.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getAccountsJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCases.getAccounts()
            .onEach { accounts ->
                _accounts.value = accounts
            }
            .launchIn(viewModelScope)
    }

    fun settingsButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.OpenTheSettingsScreen)
        }
    }

    fun selectAccountButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.OpenTheSelectAccountDialog)
        }
    }

    fun setCurrentAccount(account: Account?) {
        viewModelScope.launch {
            _currentAccount.value = account
        }
    }

    fun setCurrentDateRange(begin: LocalDate?, end: LocalDate?) {
        viewModelScope.launch {
            _currentDateRange.value = begin to end
        }
    }

    fun getPreferences() = sharedPreferences

    sealed class Event {
        object OpenTheSettingsScreen : Event()
        object OpenTheSelectAccountDialog : Event()
    }
}