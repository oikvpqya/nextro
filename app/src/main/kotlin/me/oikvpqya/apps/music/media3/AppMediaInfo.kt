package me.oikvpqya.apps.music.media3

import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.PlaybackRepeatMode
import me.oikvpqya.apps.music.model.PlaybackState

data class AppMediaInfo(
    val isPlaying: Boolean,
    val song: Library.Song?,
    val position: Long,
    val progress: Float,
    val state: PlaybackState,
    val queue: List<Library.Song>,
    val queueIndex: Int,
    val repeatMode: PlaybackRepeatMode?,
    val shuffleMode: Boolean?
) {
    companion object {
        val Idle = AppMediaInfo(
            isPlaying = false,
            song = null,
            position = 0L,
            progress = 0f,
            state = PlaybackState.IDLE,
            queue = emptyList(),
            queueIndex = 0,
            repeatMode = null,
            shuffleMode = null
        )
    }
}
