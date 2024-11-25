package com.example.mintosassignment.data.data_store.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mintosassignment.data.data_store.dao.CurrencyDao

@Database(entities = [ExchangeRatesEntity::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}