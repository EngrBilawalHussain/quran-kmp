package com.abcode.quran.audio

enum class PlaybackState { IDLE, BUFFERING, READY, ENDED }

/**
 * Cross-platform audio player abstraction. Android is backed by Media3/ExoPlayer,
 * iOS by AVPlayer and JVM by a no-op implementation.
 */
interface AudioPlayer {
    val isPlaying: Boolean
    val currentPosition: Long
    val duration: Long

    var onIsPlayingChanged: ((Boolean) -> Unit)?
    var onPlaybackStateChanged: ((PlaybackState) -> Unit)?
    var onMediaItemTransition: ((String?) -> Unit)?

    fun setMediaItem(url: String)
    fun prepare()
    fun play()
    fun pause()
    fun stop()
    fun seekTo(positionMs: Long)
    fun setPlaybackSpeed(speed: Float)
    fun release()
}

expect fun createAudioPlayer(): AudioPlayer
