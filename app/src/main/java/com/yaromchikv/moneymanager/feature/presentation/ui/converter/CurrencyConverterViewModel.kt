package com.yaromchikv.moneymanager.feature.presentation.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.feature.data.util.Resource
import com.yaromchikv.moneymanager.feature.domain.repository.ConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val repository: ConverterRepository
) : ViewModel() {

    private val _conversion = MutableStateFlow<ConversionState>(ConversionState.Idle)
    val conversion = _conversion.asStateFlow()

    fun convert(
        amount: Double,
        from: String,
        to: String
    ) {
        viewModelScope.launch {
            _conversion.value = ConversionState.Loading

            if (from == to) _conversion.value =
                ConversionState.Ready(amount.toAmountFormat(withMinus = false))
            else {
                if (from != "BYN" && to != "BYN") {
                    when (val response = repository.getCurrencyRates(from)) {
                        is Resource.Success -> {
                            val rates = response.data?.rates
                            if (rates != null) {
                                val rate = when (to) {
                                    "USD" -> rates.usd
                                    "EUR" -> rates.eur
                                    "RUB" -> rates.rub
                                    "UAH" -> rates.uah
                                    "PLN" -> rates.pln
                                    else -> null
                                }
                                if (rate != null) {
                                    val convertedAmount = amount * rate
                                    _conversion.value =
                                        ConversionState.Ready(
                                            convertedAmount.toAmountFormat(
                                                withMinus = false
                                            )
                                        )
                                } else {
                                    _conversion.value = ConversionState.Error("Unexpected error")
                                }
                            }
                        }
                        is Resource.Error -> {
                            _conversion.value =
                                ConversionState.Error(response.message ?: "Unexpected error")
                        }
                    }
                } else {
                    val fromIsByn = from == "BYN"

                    when (val response = repository.getBynRate(if (!fromIsByn) from else to)) {
                        is Resource.Success -> {
                            val rate = response.data?.rate
                            if (rate != null) {
                                val convertedAmount =
                                    if (!fromIsByn) amount * rate else amount / rate
                                _conversion.value =
                                    ConversionState.Ready(convertedAmount.toAmountFormat(withMinus = false))
                            } else {
                                _conversion.value = ConversionState.Error("Unexpected error")
                            }
                        }
                        is Resource.Error -> {
                            _conversion.value =
                                ConversionState.Error(response.message ?: "Unexpected error")
                        }
                    }
                }
            }
        }
    }

    sealed class ConversionState {
        class Ready(val result: String) : ConversionState()
        class Error(val error: String) : ConversionState()
        object Loading : ConversionState()
        object Idle : ConversionState()
    }
}