package com.example.mintosassignment.data.modals


import com.example.mintosassignment.data.data_store.entities.ExchangeRatesEntity
import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Rates
)

fun ExchangeRatesResponse.toExchangeRateEntity(): ExchangeRatesEntity = ExchangeRatesEntity(
    timestamp = System.currentTimeMillis(),
    base = base,
    currencyAmount = amount,
    date = date,
    rates =  rates.toCurrencyRates(),
)




