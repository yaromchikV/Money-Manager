package com.yaromchikv.moneymanager.feature.presentation.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.ConvertCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    private val _conversion = MutableStateFlow<ConversionState>(ConversionState.Idle)
    val conversion = _conversion.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    var resultValue: Double = 0.0

    fun convert(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            _conversion.value = ConversionState.Loading
            resultValue = convertCurrencyUseCase(amount, from, to)
            _conversion.value =
                when (resultValue) {
                    -1.0 -> ConversionState.Error("Unexpected error")
                    -2.0 -> ConversionState.Error("Check your internet connection")
                    else -> ConversionState.Ready(resultValue.toAmountFormat(withMinus = false))
                }
        }
    }

    fun convertButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.Convert)
        }
    }

    fun swapButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.Swap)
        }
    }

    fun addTransactionButtonClick(account: Account, amount: Float) {
        viewModelScope.launch {
            _events.emit(Event.OpenTheAddTransactionSheet(account, amount))
        }
    }

    sealed class ConversionState {
        class Ready(val result: String) : ConversionState()
        class Error(val error: String) : ConversionState()
        object Loading : ConversionState()
        object Idle : ConversionState()
    }

    sealed class Event {
        object Convert : Event()
        object Swap : Event()
        data class OpenTheAddTransactionSheet(val account: Account, val amount: Float) : Event()
    }
}
