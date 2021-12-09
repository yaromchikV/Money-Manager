package com.yaromchikv.moneymanager.feature.data.api

import com.yaromchikv.moneymanager.feature.domain.model.currency.BynResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BynApi {

    @GET("api/exrates/rates/{base}?parammode=2")
    suspend fun getRates(
        @Path("base") base: String
    ): Response<BynResponse>
}