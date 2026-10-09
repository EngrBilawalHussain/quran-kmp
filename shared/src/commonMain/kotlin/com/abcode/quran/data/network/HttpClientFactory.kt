package com.abcode.quran.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val AppJson: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

fun createHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(AppJson)
    }
}
