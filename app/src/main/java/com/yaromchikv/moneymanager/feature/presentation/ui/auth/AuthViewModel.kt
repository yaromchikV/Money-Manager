package com.yaromchikv.moneymanager.feature.presentation.ui.auth

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.AUTH_CODE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.FIRST_TIME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _code = MutableStateFlow("")
    val code = _code.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _tempCode = MutableStateFlow("")

    fun numberButtonClick(value: String) {
        _code.value += value
        if (_code.value.length == 4) {
            checkTheCode()
        }
    }

    private fun checkTheCode() {
        viewModelScope.launch {
            if (!isAppLaunchedFirstTime()) {
                val correctCode = sharedPreferences.getString(AUTH_CODE_KEY, "") ?: ""
                _events.emit(if (_code.value == correctCode) Event.OpenMainActivity else Event.DeleteCode)
            } else {
                checkAuth()
            }
        }
    }

    private suspend fun checkAuth() {
        if (_tempCode.value != "") {
            if (_tempCode.value == _code.value) {
                sharedPreferences.edit()
                    .putString(AUTH_CODE_KEY, _tempCode.value)
                    .putBoolean(FIRST_TIME_KEY, false)
                    .apply()
                _events.emit(Event.OpenMainActivity)
            } else {
                _tempCode.value = ""
                _events.emit(Event.DeleteCode)
                _events.emit(Event.SetThePin)
            }
        } else {
            _tempCode.value = _code.value
            _code.value = ""
            _events.emit(Event.RepeatThePin)
        }
    }

    fun clearCode() {
        _code.value = ""
    }

    fun backspaceButtonClick() {
        if (_code.value.isNotEmpty())
            _code.value = _code.value.dropLast(1)
    }

    fun forgotButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.ForgotClick)
        }
    }

    fun eraseDataClick() {
        viewModelScope.launch {
            _events.emit(Event.EraseData)
        }
    }

    fun isAppLaunchedFirstTime(): Boolean {
        return sharedPreferences.getBoolean(FIRST_TIME_KEY, true)
    }

    fun startAuthSetup() {
        viewModelScope.launch {
            _events.emit(Event.SetThePin)
        }
    }

    sealed class Event {
        object ForgotClick : Event()
        object OpenMainActivity : Event()
        object DeleteCode : Event()
        object EraseData : Event()
        object SetThePin : Event()
        object RepeatThePin : Event()
    }
}
