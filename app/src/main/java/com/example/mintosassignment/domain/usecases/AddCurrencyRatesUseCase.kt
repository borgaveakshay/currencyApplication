package com.example.mintosassignment.domain.usecases

import com.example.mintosassignment.data.common.Resource
import com.example.mintosassignment.data.common.ResourceStatus
import com.example.mintosassignment.domain.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCurrencyRatesUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {

    suspend operator fun invoke(baseCurrency: String): Flow<Resource<ResourceStatus>> {
        return currencyRepository.addLatestExchangeRates(baseCurrency)
    }

}