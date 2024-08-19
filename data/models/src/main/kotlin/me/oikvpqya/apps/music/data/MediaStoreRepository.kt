package me.oikvpqya.apps.music.data

import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library

interface MediaStoreRepository {
    val songsFlow: Flow<List<Library.Song>>
    val albumsFlow: Flow<List<Libraries.Album>>
    val artistsFlow: Flow<List<Libraries.Default>>

    fun albumSongsFlow(albumId: Long): Flow<List<Library.Song>>
    fun artistsSongsFlow(artist: String): Flow<List<Library.Song>>
}
