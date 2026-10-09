package com.abcode.quran.ui.screen.textbysurah

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.abcode.quran.data.SurahDataProvider
import com.abcode.quran.data.SurahDetails

class TextQuranbySurahViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set

    val filteredSurahs: List<SurahDetails>
        get() = if (searchQuery.isEmpty()) {
            SurahDataProvider.surahs
        } else {
            SurahDataProvider.surahs.filter { 
                it.name.contains(searchQuery, ignoreCase = true) || 
                it.englishName.contains(searchQuery, ignoreCase = true) ||
                it.number.toString() == searchQuery
            }
        }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }
}
