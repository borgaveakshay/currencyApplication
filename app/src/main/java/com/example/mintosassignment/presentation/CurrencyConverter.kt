package com.example.mintosassignment.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mintosassignment.data.common.LifecycleEventListener
import com.example.mintosassignment.data.common.TimeSchedulerManager
import com.example.mintosassignment.data.common.getFormattedDate
import com.example.mintosassignment.data.common.mapToCurrencyName
import com.example.mintosassignment.data.states.CurrencyItem
import com.example.mintosassignment.data.view_models.CurrencyViewModel
import com.example.mintosassignment.domain.modals.ExchangeRate
import com.example.mintosassignment.presentation.theme.md_theme_light_onPrimary
import com.example.mintosassignment.presentation.theme.md_theme_light_onPrimaryContainer
import com.example.mintosassignment.presentation.theme.md_theme_light_outline
import com.example.mintosassignment.presentation.theme.md_theme_light_primary
import com.example.mintosassignment.presentation.theme.md_theme_light_primaryContainer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverter(
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    var selectedCurrency by remember { mutableStateOf<ExchangeRate?>(ExchangeRate(baseCurrency = "EUR")) }
    var amount by remember { mutableStateOf("") }
    val exchangeRates by currencyViewModel.getExchangeRatesState.collectAsStateWithLifecycle()
    val addExchangeRatesState by currencyViewModel.addExchangeRatesState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showErrorPopupDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        currencyViewModel.getLatestExchangeRates()
    }
    LaunchedEffect(Unit) {
        currencyViewModel.addLatestExchangeRates(selectedCurrency?.baseCurrency ?: "EUR")
    }
    LifecycleEventListener { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                TimeSchedulerManager.startTimer {
                    currencyViewModel.addLatestExchangeRates(
                        exchangeRates.data?.baseCurrency ?: "EUR"
                    )
                }
            }

            Lifecycle.Event.ON_STOP -> {
                TimeSchedulerManager.stopTimer()
            }

            else -> {
                // Do nothing
            }
        }
    }

    LaunchedEffect(addExchangeRatesState.error) {
        addExchangeRatesState.error?.let { _ ->
            showErrorPopupDialog = true
        }
    }

    LaunchedEffect(selectedCurrency) {
        selectedCurrency?.let {
            if (it.baseCurrency.isNotEmpty())
                currencyViewModel.addLatestExchangeRates(it.baseCurrency)
        }
    }
    if (showErrorPopupDialog) {
        BasicAlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true
            ),
            onDismissRequest = {
                showErrorPopupDialog = false
            },

            ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = md_theme_light_onPrimaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = addExchangeRatesState.error
                            ?: "We are having some trouble fetching latest rates. Please try after sometime.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(min = 150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            showErrorPopupDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Currency Converter") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = md_theme_light_primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        if (exchangeRates.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        exchangeRates.data?.let {
            Text(
                text = "Last Updated: ${it.timeStamp?.getFormattedDate()}",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                selectedCurrency?.let {
                    item {
                        CurrencyInputCard(
                            currency = it,
                            amount = amount,
                            onAmountChange = { amount = it },
                            focusRequester = focusRequester
                        )
                    }
                }
                exchangeRates.currencyList?.let { list ->

                    items(
                        items = list.sortedBy { it.shortCurrencyName }
                            .filter { it.shortCurrencyName != selectedCurrency?.baseCurrency },
                        key = { it.shortCurrencyName }
                    ) { currency ->
                        CurrencyCard(
                            currency = currency,
                            baseAmount = amount.toDoubleOrNull() ?: 0.0,
                            baseCurrency = selectedCurrency?.baseCurrency ?: "EUR",
                            onClick = { currencyItem ->
                                selectedCurrency = ExchangeRate(
                                    baseCurrency = currencyItem.shortCurrencyName,
                                    compareAmount = amount.toIntOrNull(),
                                    currencyValue = currencyItem.rate,
                                )
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyInputCard(
    currency: ExchangeRate,
    amount: String,
    onAmountChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = md_theme_light_primary
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = currency.baseCurrency,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = md_theme_light_onPrimary
                        )

                    )
                    Text(
                        text = currency.baseCurrency.mapToCurrencyName(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = md_theme_light_onPrimary
                        )
                    )
                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    OutlinedTextField(
                        value = amount,
                        textStyle = MaterialTheme.typography.titleLarge,
                        onValueChange = onAmountChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(120.dp)
                            .padding(start = 40.dp)
                            .focusRequester(focusRequester),
                        colors = colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            focusedTextColor = md_theme_light_onPrimary,
                            unfocusedTextColor = md_theme_light_onPrimary
                        ),
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrencyCard(
    currency: CurrencyItem,
    baseAmount: Double,
    baseCurrency: String,
    onClick: (CurrencyItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onClick(currency)
            }),
        colors = CardDefaults.cardColors(
            containerColor = md_theme_light_outline
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = currency.shortCurrencyName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = currency.longCurrencyName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            AnimatedContent(
                targetState = baseAmount,
                transitionSpec = {
                    fadeIn() + slideInVertically() togetherWith
                            fadeOut() + slideOutVertically()
                }, label = ""
            ) { amount ->
                Column {
                    Text(
                        text = String.format("%.2f", amount * currency.rate),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "1 $baseCurrency = ${currency.rate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}