package me.oikvpqya.apps.music.data

import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.PlaybackRepeatMode
import me.oikvpqya.apps.music.model.SortBy
import me.oikvpqya.apps.music.model.SortOrder

interface PreferenceRepository {
    // NowPlaying
    val queueIndexFlow: Flow<Int>
    suspend fun updateQueueIndex(queueIndex: Int)

    // SongsScreen
    val songsSortByFlow: Flow<SortBy>
    val songsSortOrderFlow: Flow<SortOrder>

    // AlbumsScreen
    val albumsSortByFlow: Flow<SortBy>
    val albumsSortOrderFlow: Flow<SortOrder>

    // ArtistsScreen
    val artistsSortByFlow: Flow<SortBy>
    val artistsSortOrderFlow: Flow<SortOrder>

    // AlbumDetailScreen
    val albumDetailSortByFlow: Flow<SortBy>
    val albumDetailSortOrderFlow: Flow<SortOrder>

    // ArtistDetailScreen
    val artistDetailSortByFlow: Flow<SortBy>
    val artistDetailSortOrderFlow: Flow<SortOrder>

    // Player
    val repeatModeFlow: Flow<PlaybackRepeatMode>
    suspend fun updateRepeatMode(mode: PlaybackRepeatMode)
    val shuffleModeFlow: Flow<Boolean>
    suspend fun updateShuffleMode(enable: Boolean)
}
