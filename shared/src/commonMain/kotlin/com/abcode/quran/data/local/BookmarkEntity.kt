package com.abcode.quran.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abcode.quran.util.currentTimeMillis

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "SURAH" or "AYAH"
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int? = null,
    val arabicText: String? = null,
    val translationText: String? = null,
    val timestamp: Long = currentTimeMillis()
)
