package com.example.mintosassignment.data.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.convertToCurrencyList
import com.example.mintosassignment.data.states.AddExchangeRatesState
import com.example.mintosassignment.data.states.GetExchangeRatesState
import com.example.mintosassignment.domain.usecases.AddCurrencyRatesUseCase
import com.example.mintosassignment.domain.usecases.GetCurrencyRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val addCurrencyRatesUseCase: AddCurrencyRatesUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    val addExchangeRatesState: StateFlow<AddExchangeRatesState>
        field = MutableStateFlow(AddExchangeRatesState())

    val getExchangeRatesState: StateFlow<GetExchangeRatesState>
        field = MutableStateFlow(GetExchangeRatesState())

    fun addLatestExchangeRates(baseCurrency: String) = viewModelScope.launch {
        addCurrencyRatesUseCase(baseCurrency).collect { result ->
            when (result) {
                is Resource.Error -> {
                    addExchangeRatesState.emit(
                        AddExchangeRatesState(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    )
                }

                is Resource.Loading -> {
                    addExchangeRatesState.emit(AddExchangeRatesState(isLoading = true))
                }

                is Resource.Success -> {
                    addExchangeRatesState.emit(
                        AddExchangeRatesState(
                            data = result.data,
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    fun getLatestExchangeRates() = viewModelScope.launch {
        getCurrencyRatesUseCase().collect { result ->
            when (result) {
                is Resource.Error -> {
                    getExchangeRatesState.emit(
                        GetExchangeRatesState(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    )
                }

                is Resource.Loading -> {
                    getExchangeRatesState.emit(GetExchangeRatesState(isLoading = true))
                }

                is Resource.Success -> {
                    result.data?.let {
                        getExchangeRatesState.emit(
                            GetExchangeRatesState(
                                data = result.data,
                                currencyList = result.data.otherCurrencyRates?.convertToCurrencyList(
                                    result.data.timeStamp ?: Date().time
                                ),
                                isLoading = false
                            )
                        )
                    }
                }
            }
        }
    }
}