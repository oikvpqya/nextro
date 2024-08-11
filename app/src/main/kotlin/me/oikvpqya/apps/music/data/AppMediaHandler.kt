package me.oikvpqya.apps.music.data

import kotlinx.coroutines.flow.SharedFlow
import me.oikvpqya.apps.music.model.Library

interface AppMediaHandler {
    fun playOrPause()
    fun playSongs(items: List<Library.Song>, index: Int = 0, position: Long = 0L)
    fun playAt(index: Int, position: Long)
    fun playAt(position: Long)
    fun like(item: Library.Song?)
    fun unlike(item: Library.Song?)
    fun isFavoriteSharedFlow(item: Library.Song?): SharedFlow<Boolean>
    fun disableRepeatMode()
    fun enableRepeatOneMode()
    fun enableRepeatAllMode()
    fun enableShuffleMode()
    fun disableShuffleMode()
    fun seekToPrevious()
    fun seekToNext()
    fun setVolume(value: Float)
}
