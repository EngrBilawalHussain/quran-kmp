package com.abcode.quran

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abcode.quran.ui.screen.audioscreen.AudioQuranScreen
import com.abcode.quran.ui.screen.bookmarks.BookmarksScreen
import com.abcode.quran.ui.screen.explore.ExploreScreen
import com.abcode.quran.ui.screen.main.MainBottomNavigation
import com.abcode.quran.ui.screen.main.MainScreen
import com.abcode.quran.ui.screen.reciters.RecitersScreen
import com.abcode.quran.ui.screen.settings.SettingsScreen
import com.abcode.quran.ui.screen.textbyjuzz.TextQuranbyJuzzScreen
import com.abcode.quran.ui.screen.textbysurah.TextQuranbySurahDetailScreen
import com.abcode.quran.ui.screen.textbysurah.TextQuranbySurahScreen
import com.abcode.quran.ui.screen.textwithaudio.TextwithAudioScreen
import com.abcode.quran.ui.theme.QuranTheme

@Composable
@Preview
fun App() {
    QuranTheme {
        var currentTab by remember { mutableStateOf("home") }
        var currentScreen by remember { mutableStateOf("home") }
        var selectedSurahNumber by remember { mutableStateOf(1) }

        Scaffold(
            bottomBar = {
                if (currentScreen in listOf("home", "explore", "bookmarks", "reciters", "settings")) {
                    MainBottomNavigation(
                        currentTab = currentTab,
                        onTabSelected = {
                            currentTab = it
                            currentScreen = it
                        }
                    )
                }
            }
        ) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            when (currentScreen) {
                "home" -> MainScreen(
                    onAudioQuranClick = { currentScreen = "audio" },
                    onJuzzClick = { currentScreen = "juzz" },
                    onSurahClick = { currentScreen = "surah_list" },
                    onTextWithAudioClick = { currentScreen = "text_audio" },
                    modifier = modifier
                )
                "explore" -> ExploreScreen(onBack = { currentScreen = "home"; currentTab = "home" })
                "bookmarks" -> BookmarksScreen(onBack = { currentScreen = "home"; currentTab = "home" })
                "reciters" -> RecitersScreen(onBack = { currentScreen = "home"; currentTab = "home" })
                "settings" -> SettingsScreen(onBack = { currentScreen = "home"; currentTab = "home" })

                "audio" -> AudioQuranScreen(
                    onBack = { currentScreen = "home" }
                )
                "juzz" -> TextQuranbyJuzzScreen(
                    onBack = { currentScreen = "home" }
                )
                "surah_list" -> TextQuranbySurahScreen(
                    onBack = { currentScreen = "home" },
                    onReadClick = { surah ->
                        selectedSurahNumber = surah.number
                        currentScreen = "surah_detail"
                    }
                )
                "surah_detail" -> TextQuranbySurahDetailScreen(
                    surahNumber = selectedSurahNumber,
                    onBack = { currentScreen = "surah_list" }
                )
                "text_audio" -> TextQuranbySurahScreen(
                    onBack = { currentScreen = "home" },
                    onReadClick = { surah ->
                        selectedSurahNumber = surah.number
                        currentScreen = "text_audio_detail"
                    }
                )
                "text_audio_detail" -> TextwithAudioScreen(
                    surahNumber = selectedSurahNumber,
                    onBack = { currentScreen = "text_audio" }
                )
            }
        }
    }
}
