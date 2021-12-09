package com.yaromchikv.moneymanager.feature.domain.model.currency

import com.squareup.moshi.Json

data class BynResponse(
    @Json(name = "Cur_Abbreviation") val base: String,
    @Json(name = "Cur_OfficialRate") val rate: Double,
    @Json(name = "Cur_Scale") val scale: Int
)