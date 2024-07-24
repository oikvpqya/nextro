package me.oikvpqya.apps.music.mediastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.MediaStoreRepository
import me.oikvpqya.apps.music.data.MediaSynchronizer
import me.oikvpqya.apps.music.model.Library
import me.tatarka.inject.annotations.Inject

@Inject
class LocalMediaSynchronizer(
    private val coroutineScope: CoroutineScope,
    private val mediaStoreRepository: MediaStoreRepository,
    private val appDatabaseRepository: AppDatabaseRepository,
) : MediaSynchronizer {

    private var mediaSyncingJob: Job? = null

    override suspend fun refresh(path: String?): Boolean {
        return true
    }

    override fun startSync() {
        if (mediaSyncingJob != null) return
        mediaSyncingJob = getMediaVideosFlow().onEach { media ->
            coroutineScope.launch { updateMedia(media) }
        }.launchIn(coroutineScope)
    }

    override fun stopSync() {
        mediaSyncingJob?.cancel()
    }

    private suspend fun updateMedia(media: List<Library.Song>) {
        return withContext(Dispatchers.Default) {
            appDatabaseRepository.upsertSongs(media)
        }
    }

    private fun getMediaVideosFlow(): Flow<List<Library.Song>> {
        return mediaStoreRepository.songsFlow
    }
}
