package com.abcode.quran.ui.screen.textbysurah

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcode.quran.data.*
import kotlinx.coroutines.launch

class TextQuranbySurahDetailViewModel : ViewModel() {

    private val ummahApiService = UmmahApiService()

    var surahData by mutableStateOf<UmmahSurahData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun fetchSurahContent(number: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = ummahApiService.getSurah(number)
                if (response.success) {
                    surahData = response.data
                }
            } catch (e: Exception) {
                println(e.stackTraceToString())
            } finally {
                isLoading = false
            }
        }
    }
}
