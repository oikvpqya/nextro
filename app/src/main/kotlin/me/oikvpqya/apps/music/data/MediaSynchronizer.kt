package me.oikvpqya.apps.music.data

interface MediaSynchronizer {
    suspend fun refresh(path: String? = null): Boolean
    fun startSync()
    fun stopSync()
}
