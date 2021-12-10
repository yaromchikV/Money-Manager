package com.yaromchikv.moneymanager.feature.domain.usecases

import com.yaromchikv.moneymanager.feature.data.util.Resource
import com.yaromchikv.moneymanager.feature.domain.repository.ConverterRepository

class ConvertCurrencyUseCase(private val repository: ConverterRepository) {
    suspend operator fun invoke(amount: Double, from: String, to: String): Double {
        if (from == to) return amount
        val isOperationWithBYN = from == "BYN" || to == "BYN"

        return if (!isOperationWithBYN)
            getCurrencyRate(amount, from, to)
        else
            getBynRate(amount, from, to)
    }

    private suspend fun getCurrencyRate(amount: Double, from: String, to: String): Double {
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
            is Resource.Error -> return -2.0
        }
    }

    private suspend fun getBynRate(amount: Double, from: String, to: String): Double {
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
            is Resource.Error -> -2.0
        }
    }
}
