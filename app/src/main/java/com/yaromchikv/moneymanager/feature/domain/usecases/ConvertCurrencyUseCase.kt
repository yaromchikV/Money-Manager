package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.data.util.Resource
import com.yaromchikv.moneymanager.feature.domain.repository.ConverterRepository

class ConvertCurrencyUseCase(private val repository: ConverterRepository) {
    suspend operator fun invoke(amount: Double, from: String, to: String): Double {
        if (from == to) return amount
        if (from != "BYN" && to != "BYN") {
            when (val response = repository.getCurrencyRates(from)) {
                is Resource.Success -> {
                    val rates = response.data?.rates ?: return -1.0
                    val rate = when (to) {
                        "USD" -> rates.usd
                        "EUR" -> rates.eur
                        "RUB" -> rates.rub
                        "UAH" -> rates.uah
                        "PLN" -> rates.pln
                        else -> return -1.0
                    }
                    return amount * rate
                }
                is Resource.Error -> return -1.0
            }
        } else {
            val fromIsByn = from == "BYN"
            return when (
                val response = repository.getBynRate(
                    if (!fromIsByn) from else to
                )
            ) {
                is Resource.Success -> {
                    val data = response.data ?: return -1.0

                    if (!fromIsByn) amount * (data.rate / data.scale)
                    else amount / (data.rate / data.scale)
                }
                is Resource.Error -> -1.0
            }
        }
    }
}
