package com.yaromchikv.moneymanager.feature.presentation.ui.accounts

import com.yaromchikv.moneymanager.feature.domain.model.Account

data class AccountsState(
    val accounts: List<Account> = emptyList()
)
