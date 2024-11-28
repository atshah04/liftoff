package com.example.liftoff.ui.home

import com.example.liftoff.data.classes.Quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun motivationalquotes(): Quote? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.realinspire.tech/v1/quotes/random?minLength=75&maxLength=125")
        .build()
    return client.newCall(request).execute().use { response ->
        val resp = response.body!!.string()
        val gson = Gson()
        val quoteListType = object : TypeToken<List<Quote>>() {}.type
        val quotes: List<Quote> = gson.fromJson(resp, quoteListType)
        quotes.first()
    }
}
