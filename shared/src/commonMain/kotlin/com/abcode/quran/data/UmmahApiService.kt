package com.abcode.quran.data

import com.abcode.quran.data.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

@Serializable
data class UmmahJuzResponse(
    val success: Boolean,
    val data: UmmahJuzData
)

@Serializable
data class UmmahJuzData(
    val juz_number: Int,
    val verses: List<UmmahVerse>
)

@Serializable
data class UmmahSurahResponse(
    val success: Boolean,
    val data: UmmahSurahData
)

@Serializable
data class UmmahSurahData(
    val surah: UmmahSurahInfo,
    val verses: List<UmmahVerse>
)

@Serializable
data class UmmahSurahInfo(
    val number: Int,
    val name_arabic: String,
    val name_english: String,
    val name_translation: String,
    val revelation_place: String,
    val revelation_order: Int,
    val verses_count: Int
)

@Serializable
data class UmmahVerse(
    val verse_key: String,
    val surah_name: String? = null,
    val ayah: Int,
    val arabic: String,
    val translations: UmmahTranslations,
    val audio: UmmahVerseAudio? = null
)

@Serializable
data class UmmahTranslations(
    val sahih_international: String
)

@Serializable
data class UmmahVerseAudio(
    val ayah_audio: String
)

class UmmahApiService(
    private val client: HttpClient = createHttpClient()
) {
    suspend fun getJuz(
        number: Int,
        apiKey: String = DEFAULT_API_KEY
    ): UmmahJuzResponse = client.get("$BASE_URL" + "quran/juz/$number") {
        parameter("apikey", apiKey)
    }.body()

    suspend fun getSurah(
        number: Int,
        apiKey: String = DEFAULT_API_KEY
    ): UmmahSurahResponse = client.get("$BASE_URL" + "quran/surah/$number") {
        parameter("apikey", apiKey)
    }.body()

    companion object {
        const val BASE_URL = "https://ummahapi.com/api/"
        const val DEFAULT_API_KEY = "umh_27d0272c0923f33f7d025998bca6d28660f6a4b9"
    }
}
