package me.oikvpqya.apps.music.data

import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library

interface AppDatabaseRepository {
    val favoriteFlow: Flow<List<Library.Song.Default>>
    val historyFlow: Flow<List<Library.Song>>
    val playlistsFlow: Flow<List<Libraries.Playlist>>
    val queueFlow: Flow<List<Library.Song.Default>>
    val topAlbumsFlow: Flow<List<Libraries.Album>>
    val topArtistsFlow: Flow<List<Libraries.Default>>
    val topSongsFlow: Flow<List<Library.Song>>
    val songsFlow: Flow<List<Library.Song>>

    suspend fun createPlaylist(name: String)
    suspend fun setFavorite(mediaIds: List<Long>)
    suspend fun deleteFavorite(mediaIds: List<Long>)
    suspend fun setQueues(mediaIds: List<Long>)
    suspend fun setHistory(mediaIds: List<Long>)
    suspend fun upsertSongs(songs: List<Library.Song>)
    suspend fun deleteSongs()

    fun isFavoriteFlow(mediaId: Long): Flow<Boolean>
}
