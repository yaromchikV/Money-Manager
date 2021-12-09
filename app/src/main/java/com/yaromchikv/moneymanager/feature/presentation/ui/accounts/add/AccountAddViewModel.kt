package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.add

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.AddAccountUseCase
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.getImageViewTint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountAddViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun addAccount(account: Account) {
        addAccountUseCase(account)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.AddAccount)
        }
    }

    fun selectColorClick(image: ImageView) {
        viewModelScope.launch {
            val color = getImageViewTint(image)
            _events.emit(Event.SelectColor(color))
        }
    }

    sealed class Event {
        object AddAccount : Event()
        data class SelectColor(val color: String) : Event()
    }
}
