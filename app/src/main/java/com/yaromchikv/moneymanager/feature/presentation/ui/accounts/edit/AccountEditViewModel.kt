package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.edit

import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(
    private val accountsUseCases: AccountUseCases
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun updateAccount(account: Account) {
        accountsUseCases.updateAccount(account)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.UpdateAccount)
        }
    }


    fun selectColorClick(image: ImageView) {
        viewModelScope.launch {
            var colorId: Int? = -1
            val imageId = ImageViewCompat.getImageTintList(image)?.defaultColor
            mapOfColors.forEach { entry ->
                if (entry.value == imageId)
                    colorId = entry.key
            }
            _events.emit(Event.SelectColor(colorId))
        }
    }

    sealed class Event {
        object UpdateAccount : Event()
        data class SelectColor(val id: Int?) : Event()
    }
}