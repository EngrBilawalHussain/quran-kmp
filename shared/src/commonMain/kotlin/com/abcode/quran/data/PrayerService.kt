package com.abcode.quran.data

import com.abcode.quran.data.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PrayerService(
    private val client: HttpClient = createHttpClient()
) {
    suspend fun getTimingsByCity(
        city: String = "Lahore",
        country: String = "Pakistan",
        method: Int = 0,
        midnightMode: Int = 1
    ): PrayerResponse = client.get("$BASE_URL" + "timingsByCity") {
        parameter("city", city)
        parameter("country", country)
        parameter("method", method)
        parameter("midnightMode", midnightMode)
    }.body()

    companion object {
        const val BASE_URL = "https://api.aladhan.com/v1/"
    }
}
