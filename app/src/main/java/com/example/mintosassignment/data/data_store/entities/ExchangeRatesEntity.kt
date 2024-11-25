package com.example.mintosassignment.data.data_store.entities


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mintosassignment.data.common.getFormattedDate
import com.example.mintosassignment.domain.modals.CurrencyRates
import com.example.mintosassignment.domain.modals.ExchangeRate

@Entity(tableName = "exchange_rates")
data class ExchangeRatesEntity(
    @PrimaryKey(autoGenerate = true)
    val timestamp: Long = 0,
    @ColumnInfo("amount")
    val currencyAmount: Int,
    @ColumnInfo("base")
    val base: String,
    @ColumnInfo("date")
    val date: String,
    @Embedded val rates: CurrencyRates
)


fun ExchangeRatesEntity.toExchangeRates(): ExchangeRate = ExchangeRate(
    compareAmount = currencyAmount,
    baseCurrency = base,
    date = timestamp.getFormattedDate(),
    otherCurrencyRates = rates,
    timeStamp = timestamp,

)


