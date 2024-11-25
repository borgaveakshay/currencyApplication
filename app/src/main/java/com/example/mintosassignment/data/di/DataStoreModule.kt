package com.example.mintosassignment.data.di


import android.content.Context
import androidx.room.Room
import com.example.mintosassignment.data.data_store.dao.CurrencyDao
import com.example.mintosassignment.data.data_store.entities.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {

    @Provides
    @Singleton
    fun getCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase =
        Room.databaseBuilder(
            context = context, CurrencyDatabase::class.java, name = "currency_database"
        ).build()

    @Provides
    @Singleton
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase): CurrencyDao =
        currencyDatabase.currencyDao()


}