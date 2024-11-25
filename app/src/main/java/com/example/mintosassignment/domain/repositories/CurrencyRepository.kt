package com.example.mintosassignment.domain.repositories

import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.ResourceStatus
import com.example.mintosassignment.domain.modals.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun addLatestExchangeRates(baseCurrency: String): Flow<Resource<ResourceStatus>>
    suspend fun getLatestExchangeRates(): Flow<Resource<ExchangeRate>>

}