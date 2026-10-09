package com.abcode.quran.ui.screen.audioscreen

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

enum class CustomRepeatMode {
    OFF, ONE, TWO, THREE, INFINITE
}

class AudioQuranViewModel : ViewModel() {

    private val archiveService = ArchiveService()

    var surahs by mutableStateOf<List<ArchiveFile>>(emptyList())
        private set

    var availableQaris = SurahDataProvider.availableQaris
        private set

    var selectedQari by mutableStateOf(availableQaris.first())
        private set

    var qariSearchQuery by mutableStateOf("")

    val filteredQaris: List<Qari>
        get() = if (qariSearchQuery.isEmpty()) {
            availableQaris
        } else {
            availableQaris.filter { it.name.contains(qariSearchQuery, ignoreCase = true) }
        }

    var isLoading by mutableStateOf(false)
        private set

    var currentPlayingSurah by mutableStateOf<ArchiveFile?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var isBuffering by mutableStateOf(false)
        private set

    var currentPosition by mutableStateOf(0L)
        private set

    var totalDuration by mutableStateOf(0L)
        private set

    var playbackSpeed by mutableStateOf(1.0f)
        private set

    var repeatMode by mutableStateOf(CustomRepeatMode.OFF)
        private set

    var isSegmentRepeatEnabled by mutableStateOf(false)
        private set
    var segmentStart by mutableStateOf(0L)
    var segmentEnd by mutableStateOf(0L)
    var segmentRepeatCount by mutableStateOf(0) // 0 for infinite, > 0 for finite
    private var currentSegmentRepeatCount = 0

    private var remainingRepeats = 0

    var bookmarkedSurahs by mutableStateOf(setOf<String>())
        private set

    private lateinit var player: AudioPlayer

    init {
        player = createAudioPlayer().apply {
            onIsPlayingChanged = { playing ->
                this@AudioQuranViewModel.isPlaying = playing
            }
            onPlaybackStateChanged = { state ->
                isBuffering = state == PlaybackState.BUFFERING
                if (state == PlaybackState.ENDED) {
                    handlePlaybackEnded()
                }
            }
            onMediaItemTransition = { url ->
                currentPlayingSurah = surahs.find { getAudioUrl(it) == url }
            }
        }
        fetchSurahs()
        updateProgress()
    }

    private fun handlePlaybackEnded() {
        when (repeatMode) {
            CustomRepeatMode.ONE, CustomRepeatMode.TWO, CustomRepeatMode.THREE -> {
                if (remainingRepeats > 0) {
                    remainingRepeats--
                    player.seekTo(0)
                    player.play()
                } else {
                    playNext()
                }
            }
            CustomRepeatMode.INFINITE -> {
                player.seekTo(0)
                player.play()
            }
            CustomRepeatMode.OFF -> {
                playNext()
            }
        }
    }

    fun changeQari(qari: Qari) {
        if (selectedQari == qari) return
        selectedQari = qari
        player.stop()
        currentPlayingSurah = null
        isSegmentRepeatEnabled = false // Reset segment repeat on qari change
        surahs = emptyList() // Clear immediately to show loading or empty state
        fetchSurahs()
    }

    private fun updateProgress() {
        viewModelScope.launch {
            while (true) {
                if (isPlaying || isBuffering) {
                    currentPosition = player.currentPosition
                    totalDuration = player.duration

                    // Segment Repeat Logic
                    if (isSegmentRepeatEnabled && segmentEnd > segmentStart && currentPosition >= segmentEnd) {
                        if (segmentRepeatCount == 0 || currentSegmentRepeatCount < segmentRepeatCount) {
                            currentSegmentRepeatCount++
                            player.seekTo(segmentStart)
                        } else {
                            // Done repeating segment, disable it or move on
                            isSegmentRepeatEnabled = false
                            currentSegmentRepeatCount = 0
                        }
                    }
                }
                delay(200) // Higher precision for segment looping
            }
        }
    }

    private fun fetchSurahs() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = archiveService.getMetadata(selectedQari.identifier)

                // Extremely robust filter:
                // 1. Must be .mp3
                // 2. Must NOT be a "sample", "intro", or "metadata" file
                // 3. Must contain a sequence of digits (the surah number)
                val mp3Files = response.files.filter { file ->
                    val name = file.name.lowercase()
                    name.endsWith(".mp3") &&
                        !name.contains("sample") &&
                        !name.contains("intro") &&
                        !name.contains("meta") &&
                        Regex("\\d+").containsMatchIn(name)
                }

                // Map to ensure uniqueness by surah number and pick best quality if duplicates
                // (Archive often has different bitrates for the same file)
                surahs = mp3Files
                    .sortedByDescending { it.format?.contains("VBR") == true || it.name.contains("128") }
                    .distinctBy { file ->
                        Regex("(\\d+)").find(file.name)?.value ?: file.name
                    }
                    .sortedBy { file ->
                        val numberMatch = Regex("(\\d+)").find(file.name)
                        numberMatch?.value?.toIntOrNull() ?: 999
                    }

            } catch (e: Exception) {
                println(e.stackTraceToString())
                surahs = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun playSurah(surah: ArchiveFile) {
        val url = getAudioUrl(surah)
        if (currentPlayingSurah == surah) {
            if (isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        } else {
            resetRepeatsForNewSurah()
            isSegmentRepeatEnabled = false // Reset segment repeat for new surah
            segmentStart = 0L
            segmentEnd = 0L
            currentSegmentRepeatCount = 0
            player.setMediaItem(url)
            player.prepare()
            player.play()
        }
    }

    private fun resetRepeatsForNewSurah() {
        remainingRepeats = when (repeatMode) {
            CustomRepeatMode.ONE -> 1
            CustomRepeatMode.TWO -> 2
            CustomRepeatMode.THREE -> 3
            else -> 0
        }
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
        currentPosition = position
    }

    fun playNext() {
        val currentIndex = surahs.indexOf(currentPlayingSurah)
        if (currentIndex != -1 && currentIndex < surahs.size - 1) {
            playSurah(surahs[currentIndex + 1])
        }
    }

    fun playPrevious() {
        val currentIndex = surahs.indexOf(currentPlayingSurah)
        if (currentIndex != -1 && currentIndex > 0) {
            playSurah(surahs[currentIndex - 1])
        }
    }

    fun toggleRepeatMode() {
        repeatMode = when (repeatMode) {
            CustomRepeatMode.OFF -> CustomRepeatMode.ONE
            CustomRepeatMode.ONE -> CustomRepeatMode.TWO
            CustomRepeatMode.TWO -> CustomRepeatMode.THREE
            CustomRepeatMode.THREE -> CustomRepeatMode.INFINITE
            CustomRepeatMode.INFINITE -> CustomRepeatMode.OFF
        }

        if (currentPlayingSurah != null) {
            resetRepeatsForNewSurah()
        }
    }

    fun toggleSegmentRepeat(enabled: Boolean) {
        isSegmentRepeatEnabled = enabled
        if (enabled && currentPlayingSurah != null) {
            currentSegmentRepeatCount = 0
            player.seekTo(segmentStart)
            currentPosition = segmentStart
            player.play()
        }
    }

    fun changePlaybackSpeed(speed: Float) {
        playbackSpeed = speed
        player.setPlaybackSpeed(speed)
    }

    fun toggleBookmark(surah: ArchiveFile) {
        val current = bookmarkedSurahs.toMutableSet()
        if (current.contains(surah.name)) {
            current.remove(surah.name)
        } else {
            current.add(surah.name)
        }
        bookmarkedSurahs = current
    }

    private fun getAudioUrl(surah: ArchiveFile): String {
        return "${ArchiveService.DOWNLOAD_BASE_URL}${selectedQari.identifier}/${surah.name}"
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
