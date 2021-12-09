package com.yaromchikv.moneymanager.feature.domain.repository

import com.yaromchikv.moneymanager.feature.data.util.Resource
import com.yaromchikv.moneymanager.feature.domain.model.currency.BynResponse
import com.yaromchikv.moneymanager.feature.domain.model.currency.CurrencyResponse

interface ConverterRepository {

    suspend fun getCurrencyRates(base: String): Resource<CurrencyResponse>
    suspend fun getBynRate(base: String): Resource<BynResponse>
}
