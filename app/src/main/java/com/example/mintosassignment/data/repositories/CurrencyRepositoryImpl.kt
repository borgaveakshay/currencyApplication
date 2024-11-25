package com.example.mintosassignment.data.repositories

import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.ResourceStatus
import com.example.mintosassignment.data.common.handleApiResponse
import com.example.mintosassignment.data.data_store.entities.toExchangeRates
import com.example.mintosassignment.data.data_store.store.CurrencyDataStore
import com.example.mintosassignment.data.modals.toExchangeRateEntity
import com.example.mintosassignment.data.network.CurrencyApi
import com.example.mintosassignment.domain.modals.ExchangeRate
import com.example.mintosassignment.domain.repositories.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val dataStore: CurrencyDataStore
) : CurrencyRepository {

    override suspend fun addLatestExchangeRates(baseCurrency: String): Flow<Resource<ResourceStatus>> =
        channelFlow<Resource<ResourceStatus>> {
            send(Resource.Loading(loading = true))
            launch {
                currencyApi.getLatestExchangeRates(baseCurrency).handleApiResponse(
                    apiSuccess = { exchangeRatesResponse ->
                        exchangeRatesResponse?.let { response ->
                            launch {
                                dataStore.insertExchangeRates(response.toExchangeRateEntity())
                                send(Resource.Success(data = ResourceStatus.Success))
                            }
                        }
                    },
                    apiError = {
                        send(Resource.Error(message = it.error, data = ResourceStatus.Error))
                    }
                )
            }
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getLatestExchangeRates(): Flow<Resource<ExchangeRate>> =
        channelFlow<Resource<ExchangeRate>> {
            send(Resource.Loading(loading = true))
            launch {
                dataStore.getExchangeRates().collect { exchangeRatesEntity ->
                    exchangeRatesEntity?.let {
                        send(Resource.Success(it.toExchangeRates()))
                    }
                }
            }

        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }.flowOn(Dispatchers.IO)

}