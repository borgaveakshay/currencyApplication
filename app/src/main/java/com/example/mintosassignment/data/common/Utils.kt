package com.example.mintosassignment.data.common

import com.example.mintosassignment.data.states.CurrencyItem
import com.example.mintosassignment.domain.modals.CurrencyRates
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

suspend fun <T> Response<T>.handleApiResponse(
    apiSuccess: suspend (T?) -> Unit,
    apiError: suspend (ErrorResponse) -> Unit
) {
    when {
        this.isSuccessful -> {
            apiSuccess(this.body())
        }

        else -> {
            this.errorBody()
            apiError(this.handleErrorBody())
        }
    }
}

private fun <T> Response<T>.handleErrorBody(): ErrorResponse =
    ErrorResponse(
        error = this.errorBody()?.string() ?: "Something went wrong",
        status = this.code()
    )

data class ErrorResponse(
    val error: String,
    val status: Int
)


fun String.mapToCurrencyName(): String {
    val currencyMap = mutableListOf<Pair<String, String>>()
    currencyMap.add("AUD" to "Australian Dollar")
    currencyMap.add("USD" to "US Dollar")
    currencyMap.add("GBP" to "British Pound")
    currencyMap.add("EUR" to "Euro")
    currencyMap.add("CAD" to "Canadian Dollar")
    currencyMap.add("CHF" to "Swiss Franc")
    currencyMap.add("CNY" to "Chinese Yuan")
    currencyMap.add("CZK" to "Czech Koruna")
    currencyMap.add("INR" to "Indian Rupee")
    currencyMap.add("BGN" to "Bulgarian Lev")
    currencyMap.add("BRL" to "Brazilian Real")
    currencyMap.add("HKD" to "Hong Kong Dollar")
    currencyMap.add("DKK" to "Danish Krone")
    currencyMap.add("HUF" to "Hungarian Forint")
    currencyMap.add("IDR" to "Iraqi Dinar")
    currencyMap.add("ILS" to "Israeli Shekel")
    currencyMap.add("ISK" to "Icelandic Krona")
    currencyMap.add("JPY" to "Japanese Yen")
    currencyMap.add("KRW" to "South Korean Won")
    currencyMap.add("MXN" to "Mexican Peso")
    currencyMap.add("MYR" to "Malaysian Ringgit")
    currencyMap.add("NOK" to "Norwegian Krone")
    currencyMap.add("NZD" to "New Zealand Dollar")
    currencyMap.add("PHP" to "Philippine Peso")
    currencyMap.add("PLN" to "Polish Zloty")
    currencyMap.add("RON" to "Romanian Leu")
    currencyMap.add("SEK" to "Swedish Krona")
    currencyMap.add("SGD" to "Singapore Dollar")
    currencyMap.add("THB" to "Thai Baht")
    currencyMap.add("TRY" to "Turkish Lira")
    currencyMap.add("ZAR" to "South African Rand")
    return currencyMap.firstOrNull { this == it.first }?.second ?: ""

}

fun CurrencyRates.convertToCurrencyList(timeStamp: Long): List<CurrencyItem> =
    listOf<CurrencyItem>(
        CurrencyItem("AUD", "Australian Dollar", aUD, timeStamp = timeStamp),
        CurrencyItem("BGN", "Bulgarian Lev", bGN, timeStamp = timeStamp),
        CurrencyItem("BRL", "Brazilian Real", bRL, timeStamp = timeStamp),
        CurrencyItem("CAD", "Canadian Dollar", cAD, timeStamp = timeStamp),
        CurrencyItem("CHF", "Swiss Franc", cHF, timeStamp = timeStamp),
        CurrencyItem("CNY", "Chinese Yuan", cNY, timeStamp = timeStamp),
        CurrencyItem("CZK", "Czech Koruna", cZK, timeStamp = timeStamp),
        CurrencyItem("DKK", "Danish Krone", dKK, timeStamp = timeStamp),
        CurrencyItem("EUR", "Euro", eUR, timeStamp = timeStamp),
        CurrencyItem("GBP", "British Pound Sterling", gBP, timeStamp = timeStamp),
        CurrencyItem("HKD", "Hong Kong Dollar", hKD, timeStamp = timeStamp),
        CurrencyItem("HUF", "Hungarian Forint", hUF, timeStamp = timeStamp),
        CurrencyItem("IDR", "Indonesian Rupiah", iDR, timeStamp = timeStamp),
        CurrencyItem("ILS", "Israeli New Shekel", iLS, timeStamp = timeStamp),
        CurrencyItem("ISK", "Icelandic Krona", iSK, timeStamp = timeStamp),
        CurrencyItem("JPY", "Japanese Yen", jPY, timeStamp = timeStamp),
        CurrencyItem("KRW", "South Korean Won", kRW, timeStamp = timeStamp),
        CurrencyItem("MXN", "Mexican Peso", mXN, timeStamp = timeStamp),
        CurrencyItem("MYR", "Malaysian Ringgit", mYR, timeStamp = timeStamp),
        CurrencyItem("NOK", "Norwegian Krone", nOK, timeStamp = timeStamp),
        CurrencyItem("NZD", "New Zealand Dollar", nZD, timeStamp = timeStamp),
        CurrencyItem("PHP", "Philippine Peso", pHP, timeStamp = timeStamp),
        CurrencyItem("PLN", "Polish Zloty", pLN, timeStamp = timeStamp),
        CurrencyItem("RON", "Romanian Leu", rON, timeStamp = timeStamp),
        CurrencyItem("SEK", "Swedish Krona", sEK, timeStamp = timeStamp),
        CurrencyItem("SGD", "Singapore Dollar", sGD, timeStamp = timeStamp),
        CurrencyItem("THB", "Thai Baht", tHB, timeStamp = timeStamp),
        CurrencyItem("TRY", "Turkish Lira", tRY, timeStamp = timeStamp),
        CurrencyItem("USD", "US Dollar", uSD, timeStamp = timeStamp),
        CurrencyItem("ZAR", "South African Rand", zAR, timeStamp = timeStamp),
        CurrencyItem("INR", "Indian Rupee", iNR, timeStamp = timeStamp)
    )

fun Long.getFormattedDate(): String {
    // Create a SimpleDateFormat object with the desired format
    val sdf = SimpleDateFormat("dd/MM/yyyy : HH:mm:ss", Locale.getDefault())

    // Create a Date object using the timestamp
    val date = Date(this)

    // Format the date into the desired string
    return sdf.format(date)
}