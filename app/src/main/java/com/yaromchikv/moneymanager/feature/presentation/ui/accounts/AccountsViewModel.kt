package com.yaromchikv.moneymanager.feature.presentation.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AccountsState())
    val state: StateFlow<AccountsState> = _state

    private var getAccountsJob: Job? = null

    init {
        getAccounts()

        viewModelScope.launch {
            val account = Account(name = "default", color = R.color.red_purple)
            accountsUseCase.addAccount(account)
        }
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountsUseCase.getAccounts()
            .onEach { accounts ->
                _state.value = state.value.copy(accounts = accounts)
            }
            .launchIn(viewModelScope)
    }

//    fun onEvent(event: AccountsEvent) {
//        when (event) {
//            is AccountsEvent.DeleteAccount -> {
//                viewModelScope.launch {
//                    accountsUseCase.deleteAccount(event.account)
//                    recentlyDeletedAccount = event.account
//                }
//            }
//        }
//    }
}