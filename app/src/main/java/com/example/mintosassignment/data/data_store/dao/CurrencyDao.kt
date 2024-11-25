package com.example.mintosassignment.data.data_store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mintosassignment.data.data_store.entities.ExchangeRatesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM exchange_rates")
    fun getExchangeRates(): Flow<ExchangeRatesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)

    @Query("DELETE FROM exchange_rates WHERE timestamp != :timestamp")
    suspend fun deleteExchangeRates(timestamp: Long)

}