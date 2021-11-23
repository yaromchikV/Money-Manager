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

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts: StateFlow<List<Account>> = _accounts

    private var getAccountsJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountsUseCase.getAccounts()
            .onEach { accounts -> _accounts.value = accounts }
            .launchIn(viewModelScope)
    }
}