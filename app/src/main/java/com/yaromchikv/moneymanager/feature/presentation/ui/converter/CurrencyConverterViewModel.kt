package com.yaromchikv.moneymanager.feature.presentation.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.feature.domain.usecases.ConvertCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    private val _conversion = MutableStateFlow<ConversionState>(ConversionState.Idle)
    val conversion = _conversion.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun convert(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            _conversion.value = ConversionState.Loading
            val value = convertCurrencyUseCase(amount, from, to)
            _conversion.value = if (value != -1.0)
                ConversionState.Ready(value.toAmountFormat(withMinus = false))
            else
                ConversionState.Error("Unexpected error")
        }
    }

    fun convertButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.Convert)
        }
    }

    sealed class Event {
        object Convert : Event()
    }

    sealed class ConversionState {
        class Ready(val result: String) : ConversionState()
        class Error(val error: String) : ConversionState()
        object Loading : ConversionState()
        object Idle : ConversionState()
    }
}