package com.abcode.quran.ui.screen.textwithaudio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcode.quran.audio.AudioPlayer
import com.abcode.quran.audio.PlaybackState
import com.abcode.quran.audio.createAudioPlayer
import com.abcode.quran.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TextwithAudioViewModel : ViewModel() {

    private val ummahApiService = UmmahApiService()

    var surahData by mutableStateOf<UmmahSurahData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var currentAyahIndex by mutableStateOf(-1)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var currentPosition by mutableStateOf(0L)
        private set

    var totalDuration by mutableStateOf(0L)
        private set

    private lateinit var player: AudioPlayer

    init {
        player = createAudioPlayer().apply {
            onIsPlayingChanged = { playing ->
                this@TextwithAudioViewModel.isPlaying = playing
            }
            onPlaybackStateChanged = { state ->
                if (state == PlaybackState.ENDED) {
                    playNextAyah()
                }
            }
            onMediaItemTransition = { url ->
                val index = surahData?.verses?.indexOfFirst { it.audio?.ayah_audio == url } ?: -1
                if (index != -1) {
                    currentAyahIndex = index
                }
            }
        }
        updateProgress()
    }

    private fun updateProgress() {
        viewModelScope.launch {
            while (true) {
                if (isPlaying) {
                    currentPosition = player.currentPosition
                    totalDuration = player.duration
                }
                delay(500)
            }
        }
    }

    fun fetchSurahContent(number: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = ummahApiService.getSurah(number)
                if (response.success) {
                    surahData = response.data
                    // Automatically prepare first ayah if not playing
                    if (currentAyahIndex == -1 && response.data.verses.isNotEmpty()) {
                        currentAyahIndex = 0
                    }
                }
            } catch (e: Exception) {
                println(e.stackTraceToString())
            } finally {
                isLoading = false
            }
        }
    }

    fun playAyah(index: Int) {
        val verses = surahData?.verses ?: return
        if (index !in verses.indices) return

        val ayah = verses[index]
        val url = ayah.audio?.ayah_audio ?: return

        currentAyahIndex = index
        player.setMediaItem(url)
        player.prepare()
        player.play()
    }

    fun togglePlayPause() {
        if (currentAyahIndex == -1 && (surahData?.verses?.size ?: 0) > 0) {
            playAyah(0)
            return
        }

        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun playNextAyah() {
        val nextIndex = currentAyahIndex + 1
        if (nextIndex < (surahData?.verses?.size ?: 0)) {
            playAyah(nextIndex)
        }
    }

    fun playPreviousAyah() {
        val prevIndex = currentAyahIndex - 1
        if (prevIndex >= 0) {
            playAyah(prevIndex)
        }
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
        currentPosition = position
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
