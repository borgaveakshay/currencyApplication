package com.example.mintosassignment.datastore

import com.example.mintosassignment.ResponseUtils
import com.example.mintosassignment.data.data_store.entities.CurrencyDatabase
import com.example.mintosassignment.data.data_store.entities.ExchangeRatesEntity
import com.example.mintosassignment.data.data_store.store.CurrencyDataStore
import com.example.mintosassignment.data.modals.toExchangeRateEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class DataStoreTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: CurrencyDatabase

    @Inject
    lateinit var dataStore: CurrencyDataStore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    @Throws(Exception::class)
    fun currency_data_store_test_insert_fetch() = runBlocking {
        // GIVEN
        val exchangeRate = ResponseUtils.givenGetLatestExchangeRatesSuccess()
        val expectedRateEntity = exchangeRate.toExchangeRateEntity()
        // WHEN
        dataStore.insertExchangeRates(expectedRateEntity)
        delay(100)
        var result : ExchangeRatesEntity? = null
         dataStore.getExchangeRates().collect {
            result = it
        }
        //THEN
        assert(expectedRateEntity == result)
        database.close()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }
}