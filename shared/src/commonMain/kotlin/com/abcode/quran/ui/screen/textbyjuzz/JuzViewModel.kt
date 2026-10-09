package com.abcode.quran.ui.screen.textbyjuzz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcode.quran.data.*
import kotlinx.coroutines.launch

class JuzViewModel : ViewModel() {

    private val ummahApiService = UmmahApiService()

    var juzList = JuzDataProvider.juzList
        private set

    var selectedJuzDetails by mutableStateOf<JuzDetails?>(null)

    var juzContent by mutableStateOf<UmmahJuzData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun selectJuz(juz: JuzDetails) {
        selectedJuzDetails = juz
        fetchJuzContent(juz.number)
    }

    private fun fetchJuzContent(number: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = ummahApiService.getJuz(number)
                if (response.success) {
                    juzContent = response.data
                }
            } catch (e: Exception) {
                println(e.stackTraceToString())
            } finally {
                isLoading = false
            }
        }
    }

    fun clearSelection() {
        selectedJuzDetails = null
        juzContent = null
    }
}
