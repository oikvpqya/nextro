package me.oikvpqya.apps.music.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.media3.util.asInt
import me.oikvpqya.apps.music.media3.util.asPlaybackRepeatMode
import me.oikvpqya.apps.music.model.PlaybackRepeatMode
import me.oikvpqya.apps.music.model.SortBy
import me.oikvpqya.apps.music.model.SortOrder
import me.tatarka.inject.annotations.Inject

val QUEUE_INDEX = intPreferencesKey("queue_index")
val SONGS_SORT_BY = intPreferencesKey("songs_sort_by")
val SONGS_SORT_ORDER = intPreferencesKey("songs_sort_order")
val ARTISTS_SORT_BY = intPreferencesKey("artists_sort_by")
val ARTISTS_SORT_ORDER = intPreferencesKey("artists_sort_order")
val ALBUMS_SORT_BY = intPreferencesKey("albums_sort_by")
val ALBUMS_SORT_ORDER = intPreferencesKey("albums_sort_order")

val REPEAT_MODE = intPreferencesKey("repeat_mode")
val SHUFFLE_MODE = booleanPreferencesKey("shuffle_mode")

val Context.dataStore by preferencesDataStore(name = "settings")

@Inject
class PreferenceRepositoryImpl(
    private val context: Context,
) : PreferenceRepository {

    override val queueIndexFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[QUEUE_INDEX] ?: 0
        }

    override suspend fun updateQueueIndex(queueIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[QUEUE_INDEX] = queueIndex
        }
    }

    override val songsSortByFlow: Flow<SortBy> = flow { emit(SortBy.TITLE) }
    override val songsSortOrderFlow: Flow<SortOrder> = flow { emit(SortOrder.ASCENDING) }
    override val artistsSortByFlow: Flow<SortBy> = songsSortByFlow
    override val artistsSortOrderFlow: Flow<SortOrder> = songsSortOrderFlow
    override val albumDetailSortByFlow: Flow<SortBy> = songsSortByFlow
    override val albumDetailSortOrderFlow: Flow<SortOrder> = songsSortOrderFlow
    override val artistDetailSortByFlow: Flow<SortBy> = songsSortByFlow
    override val artistDetailSortOrderFlow: Flow<SortOrder> = songsSortOrderFlow
    override val albumsSortByFlow: Flow<SortBy> = songsSortByFlow
    override val albumsSortOrderFlow: Flow<SortOrder> = songsSortOrderFlow

    override val repeatModeFlow: Flow<PlaybackRepeatMode> = context.dataStore.data
        .map { preferences ->
            (preferences[REPEAT_MODE] ?: 0).asPlaybackRepeatMode()
        }

    override suspend fun updateRepeatMode(mode: PlaybackRepeatMode) {
        context.dataStore.edit { preferences ->
            preferences[REPEAT_MODE] = mode.asInt()
        }
    }

    override val shuffleModeFlow: Flow<Boolean> = context.dataStore.data
    .map { preferences ->
        preferences[SHUFFLE_MODE] ?: false
    }

    override suspend fun updateShuffleMode(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHUFFLE_MODE] = enable
        }
    }
}
