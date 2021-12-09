package com.yaromchikv.moneymanager.feature.domain.model.currency

import com.squareup.moshi.Json

data class CurrencyResponse(
    @Json(name = "data") val rates: Rates,
    val query: Query
)

data class Rates(
    @Json(name = "USD") val usd: Double = 1.0,
    @Json(name = "EUR") val eur: Double = 1.0,
    @Json(name = "RUB") val rub: Double = 1.0,
    @Json(name = "UAH") val uah: Double = 1.0,
    @Json(name = "PLN") val pln: Double = 1.0
)

data class Query(
    @Json(name ="base_currency") val base: String
)