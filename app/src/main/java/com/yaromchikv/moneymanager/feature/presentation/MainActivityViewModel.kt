package com.yaromchikv.moneymanager.feature.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.GetAccountsUseCase
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.CURRENCY_PREFERENCE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.MAIN_CURRENCY
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
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts = _accounts.asStateFlow()

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount = _selectedAccount.asStateFlow()

    private val _selectedDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(
        getCurrentLocalDate() to getCurrentLocalDate()
    )
    val selectedDateRange = _selectedDateRange.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getAccountsJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = getAccountsUseCase()
            .onEach { accounts -> _accounts.value = accounts }
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
            _selectedAccount.value = account
        }
    }

    fun setCurrentDateRange(begin: LocalDate?, end: LocalDate?) {
        viewModelScope.launch {
            _selectedDateRange.value = begin to end
        }
    }

    fun getCurrency() =
        sharedPreferences.getString(CURRENCY_PREFERENCE_KEY, MAIN_CURRENCY) ?: MAIN_CURRENCY

    sealed class Event {
        object OpenTheSettingsScreen : Event()
        object OpenTheSelectAccountDialog : Event()
    }
}
