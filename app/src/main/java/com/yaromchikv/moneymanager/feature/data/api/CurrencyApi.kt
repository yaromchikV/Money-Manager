package com.yaromchikv.moneymanager.feature.data.api

import com.yaromchikv.moneymanager.feature.domain.model.currency.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("api/v2/latest?apikey=c4b853b0-58f4-11ec-bb6f-af312896f70a")
    suspend fun getRates(
        @Query("base_currency") base: String
    ): Response<CurrencyResponse>
}
