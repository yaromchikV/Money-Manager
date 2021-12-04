package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.add

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.PRIMARY_COLOR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountAddViewModel @Inject constructor(
    private val accountsUseCases: AccountUseCases
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun addAccount(account: Account) {
        accountsUseCases.addAccount(account)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.AddAccount)
        }
    }

    fun selectColorClick(image: ImageView) {
        viewModelScope.launch {
            val colorInt = image.imageTintList?.defaultColor
            val color = if (colorInt != null) String.format(
                "#%06X",
                0xFFFFFF and colorInt
            ) else PRIMARY_COLOR
            _events.emit(Event.SelectColor(color))
        }
    }

    sealed class Event {
        object AddAccount : Event()
        data class SelectColor(val color: String) : Event()
    }
}