package com.example.mintosassignment.domain.usecases

import com.example.mintosassignment.domain.repositories.CurrencyRepository
import jakarta.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {
    suspend operator fun invoke() = currencyRepository.getLatestExchangeRates()
}