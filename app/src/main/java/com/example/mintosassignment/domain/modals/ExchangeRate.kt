package com.example.mintosassignment.domain.modals

data class ExchangeRate(
    val baseCurrency: String,
    val compareAmount: Int? = null,
    val date: String?= null,
    val currencyValue: Double? = null,
    val otherCurrencyRates: CurrencyRates? = null,
    val timeStamp: Long? = null
)



