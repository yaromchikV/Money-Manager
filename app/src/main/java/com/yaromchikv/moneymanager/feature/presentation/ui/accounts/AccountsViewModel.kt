package com.yaromchikv.moneymanager.feature.presentation.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
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
    private val accountUseCase: AccountUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts: StateFlow<List<Account>> = _accounts

    private var getAccountsJob: Job? = null

    init {
        getAccounts()

//        viewModelScope.launch {
//            val account1 = Account(name = "first", color = R.color.blue_green)
//            val account2 = Account(name = "second", color = R.color.yellow)
//            accountUseCase.addAccount(account1)
//            accountUseCase.addAccount(account2)
//
//            val cat1 = Category(name = "cat1", iconColor = R.color.blue_green, icon = R.drawable.ic_bank)
//            val cat2 = Category(name = "cat2", iconColor = R.color.yellow, icon = R.drawable.ic_calendar)
//            categoryUseCases.addCategory(cat1)
//            categoryUseCases.addCategory(cat2)
//        }
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCase.getAccounts()
            .onEach { accounts -> _accounts.value = accounts }
            .launchIn(viewModelScope)
    }
}