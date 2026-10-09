package com.abcode.quran.audio

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
private class IosAudioPlayer : AudioPlayer {

    override var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    override var onPlaybackStateChanged: ((PlaybackState) -> Unit)? = null
    override var onMediaItemTransition: ((String?) -> Unit)? = null

    private var player: AVPlayer? = null
    private var playing = false

    private val endObserver: Any? = NSNotificationCenter.defaultCenter.addObserverForName(
        name = AVPlayerItemDidPlayToEndTimeNotification,
        `object` = null,
        queue = NSOperationQueue.mainQueue,
    ) { _ ->
        playing = false
        onIsPlayingChanged?.invoke(false)
        onPlaybackStateChanged?.invoke(PlaybackState.ENDED)
    }

    override val isPlaying: Boolean get() = playing

    override val currentPosition: Long
        get() {
            val time = player?.currentTime() ?: return 0L
            val seconds = CMTimeGetSeconds(time)
            return if (seconds.isNaN()) 0L else (seconds * 1000.0).toLong()
        }

    override val duration: Long
        get() {
            val time = player?.currentItem?.duration ?: return 0L
            val seconds = CMTimeGetSeconds(time)
            return if (seconds.isNaN() || seconds.isInfinite()) 0L else (seconds * 1000.0).toLong()
        }

    override fun setMediaItem(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        val item = AVPlayerItem(uRL = nsUrl)
        val current = player
        if (current == null) {
            player = AVPlayer(playerItem = item)
        } else {
            current.replaceCurrentItemWithPlayerItem(item)
        }
        onMediaItemTransition?.invoke(url)
        onPlaybackStateChanged?.invoke(PlaybackState.READY)
    }

    override fun prepare() {
        onPlaybackStateChanged?.invoke(PlaybackState.READY)
    }

    override fun play() {
        player?.play()
        playing = true
        onIsPlayingChanged?.invoke(true)
    }

    override fun pause() {
        player?.pause()
        playing = false
        onIsPlayingChanged?.invoke(false)
    }

    override fun stop() {
        player?.pause()
        player?.seekToTime(CMTimeMakeWithSeconds(0.0, 1))
        playing = false
        onIsPlayingChanged?.invoke(false)
        onPlaybackStateChanged?.invoke(PlaybackState.IDLE)
    }

    override fun seekTo(positionMs: Long) {
        player?.seekToTime(CMTimeMakeWithSeconds(positionMs / 1000.0, 1000))
    }

    override fun setPlaybackSpeed(speed: Float) {
        player?.rate = speed
    }

    override fun release() {
        endObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(it) }
        player?.pause()
        player = null
        playing = false
    }
}

actual fun createAudioPlayer(): AudioPlayer = IosAudioPlayer()
