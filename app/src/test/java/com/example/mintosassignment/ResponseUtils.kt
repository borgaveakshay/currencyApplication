package com.example.mintosassignment

import com.example.mintosassignment.data.modals.ExchangeRatesResponse
import com.google.gson.Gson

object ResponseUtils {
    private val gson = Gson()
    fun givenGetLatestExchangeRatesSuccess(): ExchangeRatesResponse {
        val mockJsonResponse = """
            {
              "amount": 1,
              "base": "INR",
              "date": "2024-11-20",
              "rates": {
                "AUD": 0.01821,
                "BGN": 0.02195,
                "BRL": 0.06843,
                "CAD": 0.01657,
                "CHF": 0.01048,
                "CNY": 0.08589,
                "CZK": 0.28379,
                "DKK": 0.08372,
                "EUR": 0.01122,
                "GBP": 0.00936,
                "HKD": 0.09226,
                "HUF": 4.5971,
                "IDR": 188.44,
                "ILS": 0.04423,
                "ISK": 1.633,
                "JPY": 1.8453,
                "KRW": 16.5477,
                "MXN": 0.2391,
                "MYR": 0.05301,
                "NOK": 0.13068,
                "NZD": 0.02014,
                "PHP": 0.69901,
                "PLN": 0.04861,
                "RON": 0.05585,
                "SEK": 0.13025,
                "SGD": 0.01591,
                "THB": 0.41056,
                "TRY": 0.40855,
                "USD": 0.01185,
                "ZAR": 0.21466
              }
            }
        """
        val exchangeRatesResponse =
            gson.fromJson<ExchangeRatesResponse>(
                mockJsonResponse,
                ExchangeRatesResponse::class.java
            )
        return exchangeRatesResponse

    }

    fun getJsonResponse(): String =
        """
            {
              "amount": 1,
              "base": "INR",
              "date": "2024-11-20",
              "rates": {
                "AUD": 0.01821,
                "BGN": 0.02195,
                "BRL": 0.06843,
                "CAD": 0.01657,
                "CHF": 0.01048,
                "CNY": 0.08589,
                "CZK": 0.28379,
                "DKK": 0.08372,
                "EUR": 0.01122,
                "GBP": 0.00936,
                "HKD": 0.09226,
                "HUF": 4.5971,
                "IDR": 188.44,
                "ILS": 0.04423,
                "ISK": 1.633,
                "JPY": 1.8453,
                "KRW": 16.5477,
                "MXN": 0.2391,
                "MYR": 0.05301,
                "NOK": 0.13068,
                "NZD": 0.02014,
                "PHP": 0.69901,
                "PLN": 0.04861,
                "RON": 0.05585,
                "SEK": 0.13025,
                "SGD": 0.01591,
                "THB": 0.41056,
                "TRY": 0.40855,
                "USD": 0.01185,
                "ZAR": 0.21466
              }
            }
        """
}