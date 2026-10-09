package com.abcode.quran.data

import kotlinx.serialization.Serializable

@Serializable
data class PrayerResponse(
    val code: Int,
    val status: String,
    val data: PrayerData
)

@Serializable
data class PrayerData(
    val timings: PrayerTimings
)

@Serializable
data class PrayerTimings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String
)
