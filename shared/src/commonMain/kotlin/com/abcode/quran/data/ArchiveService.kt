package com.abcode.quran.data

import com.abcode.quran.data.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArchiveService(
    private val client: HttpClient = createHttpClient()
) {
    suspend fun getMetadata(identifier: String): ArchiveResponse =
        client.get("$BASE_URL" + "metadata/$identifier").body()

    companion object {
        const val BASE_URL = "https://archive.org/"
        const val DOWNLOAD_BASE_URL = "https://archive.org/download/"
    }
}
