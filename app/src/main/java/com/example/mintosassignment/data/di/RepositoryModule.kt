package com.example.mintosassignment.data.di

import com.example.mintosassignment.data.data_store.store.CurrencyDataStore
import com.example.mintosassignment.data.network.CurrencyApi
import com.example.mintosassignment.data.repositories.CurrencyRepositoryImpl
import com.example.mintosassignment.domain.repositories.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        currencyApi: CurrencyApi,
        currencyDataStore: CurrencyDataStore
    ): CurrencyRepository = CurrencyRepositoryImpl(currencyApi, currencyDataStore)
}