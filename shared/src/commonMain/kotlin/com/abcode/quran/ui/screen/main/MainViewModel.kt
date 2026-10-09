package com.abcode.quran.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WbCloudy
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcode.quran.data.PrayerService
import com.abcode.quran.data.PrayerTimings
import kotlinx.coroutines.launch

data class PrayerTime(val name: String, val time: String, val icon: ImageVector)

class MainViewModel : ViewModel() {

    private val prayerService = PrayerService()

    var prayerTimes by mutableStateOf<List<PrayerTime>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchPrayerTimes()
    }

    private fun fetchPrayerTimes() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = prayerService.getTimingsByCity()
                prayerTimes = formatTimings(response.data.timings)
            } catch (e: Exception) {
                println(e.stackTraceToString())
                // Fallback or handle error
            } finally {
                isLoading = false
            }
        }
    }

    private fun formatTimings(timings: PrayerTimings): List<PrayerTime> {
        return listOf(
            PrayerTime("Fajr (فجر)", convertTo12Hour(timings.Fajr), Icons.Rounded.WbTwilight),
            PrayerTime("Sunrise (شروق)", convertTo12Hour(timings.Sunrise), Icons.Rounded.WbSunny),
            PrayerTime("Dhuhr (ظهر)", convertTo12Hour(timings.Dhuhr), Icons.Rounded.WbSunny),
            PrayerTime("Asr (عصر)", convertTo12Hour(timings.Asr), Icons.Rounded.WbCloudy),
            PrayerTime("Maghrib (مغرب)", convertTo12Hour(timings.Maghrib), Icons.Rounded.WbTwilight),
            PrayerTime("Isha (عشاء)", convertTo12Hour(timings.Isha), Icons.Rounded.NightsStay)
        )
    }

    private fun convertTo12Hour(time24: String): String {
        val parts = time24.trim().split(":")
        if (parts.size < 2) return time24
        val hour = parts[0].toIntOrNull() ?: return time24
        val minute = parts[1].take(2).toIntOrNull() ?: return time24
        val period = if (hour < 12) "AM" else "PM"
        val hour12 = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return "$hour12:${minute.toString().padStart(2, '0')} $period"
    }
}
