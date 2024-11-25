package com.example.mintosassignment.data.states

import com.example.mintosassignment.domain.modals.ExchangeRate

data class GetExchangeRatesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: ExchangeRate? = null,
    val currencyList: List<CurrencyItem>? = null
)
