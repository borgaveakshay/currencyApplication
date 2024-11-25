package com.example.mintosassignment.usecases

import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.ResourceStatus
import com.example.mintosassignment.domain.repositories.CurrencyRepository
import com.example.mintosassignment.domain.usecases.AddCurrencyRatesUseCase
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
class AddCurrencyRatesUseCaseTest {

    private lateinit var addCurrencyRatesUseCase: AddCurrencyRatesUseCase
    private lateinit var mockCurrencyRepository: CurrencyRepository
    private lateinit var gson: Gson

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockCurrencyRepository = mockk()
        addCurrencyRatesUseCase = AddCurrencyRatesUseCase(mockCurrencyRepository)
        gson = Gson()
    }

    @Test
    fun `test addLatestExchangeRates success`() = runBlocking {
        // GIVEN
        val baseCurrency = "INR"
        val expectedResult = ResourceStatus.Success
        coEvery { mockCurrencyRepository.addLatestExchangeRates(baseCurrency) } returns flow {
            emit(
                Resource.Success(
                    data = expectedResult,
                )
            )
        }
        // WHEN
        var result: Resource<ResourceStatus>? = null
        addCurrencyRatesUseCase(baseCurrency).collect {
            result = it
        }
        // THEN
        assert(result is Resource.Success)
        assert(result?.data == expectedResult)
    }

    @Test
    fun `test addLatestExchangeRates failure`() = runBlocking {
        // GIVEN
        val baseCurrency = "INR"
        val expectedResult = "Error fetching exchange rates"
        val expectedData = ResourceStatus.Error
        coEvery { mockCurrencyRepository.addLatestExchangeRates(baseCurrency) } returns flow {
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
        var result: Resource<ResourceStatus>? = null
        addCurrencyRatesUseCase(baseCurrency).collect {
            result = it
        }
        // THEN
        assert(result is Resource.Error)
        assert(result?.message == expectedResult)
        assert(result?.data == expectedData)
    }

    @Test
    fun `test addLatestExchangeRates loading`() = runBlocking {
        // GIVEN
        val baseCurrency = "INR"
        val expectedLoadingResult = true
        coEvery { mockCurrencyRepository.addLatestExchangeRates(baseCurrency) } returns flow {
            emit(
                Resource.Loading(
                    loading =
                    expectedLoadingResult
                )
            )
        }
        // WHEN
        var result: Resource<ResourceStatus>? = null
        addCurrencyRatesUseCase(baseCurrency).collect {
            result = it
        }
        // THEN
        assert(result is Resource.Loading)
        assert(result?.loading == expectedLoadingResult)

    }

}