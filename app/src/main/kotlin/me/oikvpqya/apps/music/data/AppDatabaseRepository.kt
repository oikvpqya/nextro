package me.oikvpqya.apps.music.data

import androidx.media3.common.MediaItem
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

    suspend fun createPlaylist(name: String)
    suspend fun setFavorite(songs: List<MediaItem>)
    suspend fun deleteFavorite(songs: List<MediaItem>)
    suspend fun setQueues(songs: List<MediaItem>)
    suspend fun setHistory(songs: List<MediaItem>)

    fun isFavoriteFlow(song: MediaItem): Flow<Boolean>
}
