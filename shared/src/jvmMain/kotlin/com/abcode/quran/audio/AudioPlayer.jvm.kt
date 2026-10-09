package com.abcode.quran.audio

private class NoopAudioPlayer : AudioPlayer {

    override var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    override var onPlaybackStateChanged: ((PlaybackState) -> Unit)? = null
    override var onMediaItemTransition: ((String?) -> Unit)? = null

    override val isPlaying: Boolean get() = false
    override val currentPosition: Long get() = 0L
    override val duration: Long get() = 0L

    override fun setMediaItem(url: String) {
        onMediaItemTransition?.invoke(url)
        onPlaybackStateChanged?.invoke(PlaybackState.READY)
    }

    override fun prepare() {}
    override fun play() {}
    override fun pause() {}
    override fun stop() {}
    override fun seekTo(positionMs: Long) {}
    override fun setPlaybackSpeed(speed: Float) {}
    override fun release() {}
}

actual fun createAudioPlayer(): AudioPlayer = NoopAudioPlayer()
