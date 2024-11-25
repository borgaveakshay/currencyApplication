package com.example.mintosassignment.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mintosassignment.ResponseUtils
import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.ResourceStatus
import com.example.mintosassignment.data.common.convertToCurrencyList
import com.example.mintosassignment.data.data_store.entities.toExchangeRates
import com.example.mintosassignment.data.modals.toExchangeRateEntity
import com.example.mintosassignment.data.view_models.CurrencyViewModel
import com.example.mintosassignment.domain.repositories.CurrencyRepository
import com.example.mintosassignment.domain.usecases.AddCurrencyRatesUseCase
import com.example.mintosassignment.domain.usecases.GetCurrencyRatesUseCase
import com.example.mintosassignment.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CurrencyViewModel
    private lateinit var addCurrencyRatesUseCase: AddCurrencyRatesUseCase
    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        addCurrencyRatesUseCase = mockk()
        getCurrencyRatesUseCase = mockk()
        currencyRepository = mockk()
        viewModel = CurrencyViewModel(addCurrencyRatesUseCase, getCurrencyRatesUseCase)
    }


    @Test
    fun `test addCurrencyRates`() = testCoroutineRule.runTest {
        // GIVEN
        val baseCurrency = "INR"
        coEvery { addCurrencyRatesUseCase(baseCurrency) } returns flow<Resource<ResourceStatus>> {
            emit(Resource.Success(data = ResourceStatus.Success))
        }
        // WHEN
        viewModel.addLatestExchangeRates(baseCurrency)
        delay(100)
        val result = viewModel.addExchangeRatesState.value

        // THEN
        assert(result.data == ResourceStatus.Success)
        assert(result.error == null)
        assert(result.isLoading == false)
    }

    @Test
    fun `test addCurrencyRates error`() = testCoroutineRule.runTest {
        // GIVEN
        val baseCurrency = "INR"
        coEvery { addCurrencyRatesUseCase(baseCurrency) } returns flow<Resource<ResourceStatus>> {
            emit(Resource.Error(message = "Error"))
        }
        // WHEN
        viewModel.addLatestExchangeRates(baseCurrency)
        delay(100)
        val result = viewModel.addExchangeRatesState.value

        // THEN
        assert(result.isLoading == false)
        assert(result.error == "Error")
        assert(result.data == null)
    }


    @Test
    fun `test getCurrencyRates`() = testCoroutineRule.runTest {
        // GIVEN
        val expectedEntity =
            ResponseUtils.givenGetLatestExchangeRatesSuccess().toExchangeRateEntity()
        val currencyItems =
            expectedEntity.toExchangeRates().otherCurrencyRates?.convertToCurrencyList(
                expectedEntity.timestamp
            )
        coEvery { getCurrencyRatesUseCase() } returns flow {
            emit(Resource.Success(data = expectedEntity.toExchangeRates()))
        }
        // WHEN
        viewModel.getLatestExchangeRates()
        delay(100)
        val result = viewModel.getExchangeRatesState.value
        // THEN
        assert(result.isLoading == false)
        assert(result.currencyList == currencyItems)
        assert(result.data == expectedEntity.toExchangeRates())
        assert(result.error == null)
    }

    @Test
    fun `test getCurrencyRates error`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { getCurrencyRatesUseCase() } returns flow {
            emit(Resource.Error(message = "Error"))
        }
        // WHEN
        viewModel.getLatestExchangeRates()
        delay(100)
        val result = viewModel.getExchangeRatesState.value
        // THEN
        assert(result.isLoading == false)
        assert(result.error == "Error")
        assert(result.currencyList == null)
        assert(result.data == null)

    }


}