package com.example.mintosassignment.usecases

import com.example.mintosassignment.ResponseUtils.givenGetLatestExchangeRatesSuccess
import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.data_store.entities.toExchangeRates
import com.example.mintosassignment.data.modals.toExchangeRateEntity
import com.example.mintosassignment.domain.modals.ExchangeRate
import com.example.mintosassignment.domain.repositories.CurrencyRepository
import com.example.mintosassignment.domain.usecases.GetCurrencyRatesUseCase
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCurrencyRatesUseCaseTest {

    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase
    private lateinit var mockCurrencyRepository: CurrencyRepository
    private lateinit var gson: Gson

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockCurrencyRepository = mockk()
        getCurrencyRatesUseCase = GetCurrencyRatesUseCase(mockCurrencyRepository)
        gson = Gson()
    }

    @Test
    fun `test getLatestExchangeRates success`() = runBlocking {
        // GIVEN
        val expectedResult =
            givenGetLatestExchangeRatesSuccess().toExchangeRateEntity().toExchangeRates()
        coEvery { mockCurrencyRepository.getLatestExchangeRates() } returns flow {
            emit(
                Resource.Success(
                    data = expectedResult,
                )
            )
        }
        // WHEN
        var result: Resource<ExchangeRate>? = null
        getCurrencyRatesUseCase().collect {
            result = it
        }
        // THEN
        assert(result is Resource.Success)
        assert(result?.data == expectedResult)
    }

    @Test
    fun `test getLatestExchangeRates failure`() = runBlocking {
        // GIVEN
        val expectedResult = "Error fetching exchange rates"
        val expectedData = null
        coEvery { mockCurrencyRepository.getLatestExchangeRates() } returns flow {
            emit(
                Resource.Error(
                    message =
                    expectedResult,
                    errorCode = 404,
                    data = expectedData
                )
            )
        }

        // WHEN
        var result: Resource<ExchangeRate>? = null
        getCurrencyRatesUseCase().collect {
            result = it
        }
        // THEN
        assert(result is Resource.Error)
        assert(result?.message == expectedResult)
        assert(result?.data == expectedData)
    }

    @Test
    fun `test getLatestExchangeRates loading`() = runBlocking {
        // GIVEN
        val expectedLoadingResult = true
        coEvery { mockCurrencyRepository.getLatestExchangeRates() } returns flow {
            emit(
                Resource.Loading(
                    loading =
                    expectedLoadingResult
                )
            )
        }
        // WHEN
        var result: Resource<ExchangeRate>? = null
        getCurrencyRatesUseCase().collect {
            result = it
        }
        // THEN
        assert(result is Resource.Loading)
        assert(result?.loading == expectedLoadingResult)

    }

}