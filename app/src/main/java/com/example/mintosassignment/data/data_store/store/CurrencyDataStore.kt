package com.example.mintosassignment.data.data_store.store

import com.example.mintosassignment.data.data_store.dao.CurrencyDao
import com.example.mintosassignment.data.data_store.entities.ExchangeRatesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyDataStore @Inject constructor(private val currencyDao: CurrencyDao) {

    suspend fun getExchangeRates() = withContext(Dispatchers.IO) {
        currencyDao.getExchangeRates()
    }

    suspend fun insertExchangeRates(entity: ExchangeRatesEntity) =
        withContext(Dispatchers.IO) {
            currencyDao.insertExchangeRates(entity)
            delay(100)
            currencyDao.deleteExchangeRates(entity.timestamp)
        }

}