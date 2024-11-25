package com.example.mintosassignment.repository

import com.example.mintosassignment.ResponseUtils
import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.ResourceStatus
import com.example.mintosassignment.data.data_store.entities.toExchangeRates
import com.example.mintosassignment.data.data_store.store.CurrencyDataStore
import com.example.mintosassignment.data.modals.ExchangeRatesResponse
import com.example.mintosassignment.data.modals.toExchangeRateEntity
import com.example.mintosassignment.data.network.CurrencyApi
import com.example.mintosassignment.data.repositories.CurrencyRepositoryImpl
import com.example.mintosassignment.domain.modals.ExchangeRate
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CurrencyRepositoryTest {
    lateinit var currencyRepository: CurrencyRepositoryImpl
    lateinit var currencyDataStore: CurrencyDataStore
    lateinit var currencyApi: CurrencyApi

    @Before
    fun before() {
        currencyDataStore = mockk()
        currencyApi = mockk()
        currencyRepository =
            CurrencyRepositoryImpl(dataStore = currencyDataStore, currencyApi = currencyApi)
    }

    @Test
    fun `add latest currency rates success`() = runTest {
        // GIVEN
        val baseCurrency = "INR"
        val expectedResult = Resource.Success(data = ResourceStatus.Success)
        val expectedResponse = ResponseUtils.givenGetLatestExchangeRatesSuccess()
        coEvery { currencyDataStore.insertExchangeRates(any()) } returns Unit
        coEvery { currencyApi.getLatestExchangeRates(baseCurrency) } returns Response.success(
            expectedResponse
        )
        // WHEN
        var result: Resource<ResourceStatus>? = null
        currencyRepository.addLatestExchangeRates(baseCurrency).collect {
            result = it
        }
        // THEN
        assert(result is Resource.Success)
        assert(result?.data == expectedResult.data)
    }

    @Test
    fun `add latest currency rates failure`() = runTest {
        // GIVEN
        val baseCurrency = "INR"
        val expectedResult =
            Resource.Error<ExchangeRatesResponse>(message = "Error fetching exchange rates")
        coEvery { currencyApi.getLatestExchangeRates(baseCurrency) } returns Response.error(
            404,
            "Error fetching exchange rates".toResponseBody("application/json".toMediaTypeOrNull())
        )
        // WHEN
        var result: Resource<ResourceStatus>? = null
        currencyRepository.addLatestExchangeRates(baseCurrency).collect {
            result = it
        }
        // THEN
        assert(result is Resource.Error)
        assert(result?.message == expectedResult.message)

    }

    @Test
    fun `get latest currency rates success`() = runTest {
        // GIVEN
        val expectedEntity =
            ResponseUtils.givenGetLatestExchangeRatesSuccess().toExchangeRateEntity()
        val expectedResult = Resource.Success(
            data = expectedEntity.toExchangeRates()
        )
        coEvery { currencyDataStore.insertExchangeRates(expectedEntity) } returns Unit
        coEvery { currencyDataStore.getExchangeRates() } returns flow {
            emit(expectedEntity)
        }
        // WHEN
        var result: Resource<ExchangeRate>? = null
        currencyRepository.getLatestExchangeRates().collect {
            result = it
        }
        // THEN
        assert(result is Resource.Success)
        assert(result?.data == expectedResult.data)

    }

    @Test
    fun `get latest currency rates failure`() = runTest {
        // GIVEN
        val expectedErrorMessage = "Error fetching exchange rates"
        val expectedResult =
            Resource.Error<ExchangeRatesResponse>(message = expectedErrorMessage)
        coEvery { currencyDataStore.getExchangeRates() } throws Exception(expectedErrorMessage)

        // WHEN
        var result: Resource<ExchangeRate>? = null
        currencyRepository.getLatestExchangeRates().collect {
            result = it
        }

        // THEN
        assert(result is Resource.Error)
        assert(result?.message == expectedResult.message)
    }


}