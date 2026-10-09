package com.abcode.quran.data

import kotlinx.serialization.Serializable

@Serializable
data class ArchiveResponse(
    val files: List<ArchiveFile> = emptyList()
)

@Serializable
data class ArchiveFile(
    val name: String,
    val title: String? = null,
    val format: String? = null,
    val length: String? = null
)
