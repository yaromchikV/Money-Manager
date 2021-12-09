package com.yaromchikv.moneymanager.feature.data.repository

import android.content.Context
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.feature.data.api.BynApi
import com.yaromchikv.moneymanager.feature.data.api.CurrencyApi
import com.yaromchikv.moneymanager.feature.data.util.Resource
import com.yaromchikv.moneymanager.feature.domain.model.currency.BynResponse
import com.yaromchikv.moneymanager.feature.domain.model.currency.CurrencyResponse
import com.yaromchikv.moneymanager.feature.domain.repository.ConverterRepository

class ConverterRepositoryImpl(
    private val context: Context,
    private val currencyApi: CurrencyApi,
    private val bynApi: BynApi
) : ConverterRepository {

    override suspend fun getCurrencyRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = currencyApi.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.connection_error))
        }
    }

    override suspend fun getBynRate(base: String): Resource<BynResponse> {
        return try {
            val response = bynApi.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.connection_error))
        }
    }
}