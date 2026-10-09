package com.abcode.quran.audio

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.abcode.quran.data.AudioCache

@OptIn(UnstableApi::class)
private class AndroidAudioPlayer(context: Context) : AudioPlayer {

    override var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    override var onPlaybackStateChanged: ((PlaybackState) -> Unit)? = null
    override var onMediaItemTransition: ((String?) -> Unit)? = null

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(AudioCache.getDataSourceFactory(context)))
        .build()
        .apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    this@AndroidAudioPlayer.onIsPlayingChanged?.invoke(isPlaying)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    val state = when (playbackState) {
                        Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                        Player.STATE_READY -> PlaybackState.READY
                        Player.STATE_ENDED -> PlaybackState.ENDED
                        else -> PlaybackState.IDLE
                    }
                    this@AndroidAudioPlayer.onPlaybackStateChanged?.invoke(state)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    this@AndroidAudioPlayer.onMediaItemTransition?.invoke(mediaItem?.mediaId)
                }
            })
        }

    override val isPlaying: Boolean get() = exoPlayer.isPlaying
    override val currentPosition: Long get() = exoPlayer.currentPosition
    override val duration: Long get() = exoPlayer.duration.coerceAtLeast(0L)

    override fun setMediaItem(url: String) {
        exoPlayer.setMediaItem(MediaItem.Builder().setUri(url).setMediaId(url).build())
    }

    override fun prepare() {
        exoPlayer.prepare()
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    override fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
    }

    override fun release() {
        exoPlayer.release()
    }
}

actual fun createAudioPlayer(): AudioPlayer = AndroidAudioPlayer(AppContext.requireContext())
