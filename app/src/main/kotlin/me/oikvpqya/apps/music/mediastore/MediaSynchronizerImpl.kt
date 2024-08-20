package me.oikvpqya.apps.music.mediastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.MediaStoreRepository
import me.oikvpqya.apps.music.data.MediaSynchronizer
import me.oikvpqya.apps.music.model.Library
import me.tatarka.inject.annotations.Inject

@Inject
class MediaSynchronizerImpl(
    private val coroutineScope: CoroutineScope,
    private val mediaStoreRepository: MediaStoreRepository,
    private val appDatabaseRepository: AppDatabaseRepository,
) : MediaSynchronizer {

    private var mediaSyncingJob: Job? = null
    private val songsFlow: Flow<List<Library.Song>>
        get() = mediaStoreRepository.songsFlow

    override fun startSync() {
        if (mediaSyncingJob != null) return
        mediaSyncingJob = songsFlow.onEach { songs ->
            appDatabaseRepository.deleteAndInsertSongs(songs)
        }.launchIn(coroutineScope)
    }

    override fun stopSync() {
        mediaSyncingJob?.cancel()
    }
}
