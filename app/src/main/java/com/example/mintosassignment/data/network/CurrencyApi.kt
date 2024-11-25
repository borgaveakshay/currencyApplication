package com.example.mintosassignment.data.network

import com.example.mintosassignment.data.modals.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/latest")
    suspend fun getLatestExchangeRates(
        @Query("base") baseCurrency: String
    ): Response<ExchangeRatesResponse>

}