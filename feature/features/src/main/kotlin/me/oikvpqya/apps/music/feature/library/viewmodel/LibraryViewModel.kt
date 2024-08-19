package me.oikvpqya.apps.music.feature.library.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.MediaStoreRepository
import me.oikvpqya.apps.music.model.Library
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class LibraryViewModel(
    private val databaseRepository: AppDatabaseRepository,
    private val mediaStoreRepository: MediaStoreRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val name: String
        get() = savedStateHandle["name"] ?: "NAME"
    val summary: String
        get() = savedStateHandle["summary"] ?: "SUMMARY"
    val albumId: Long
        get() = savedStateHandle["albumId"] ?: -1L

    val albumsSharedFlow = mediaStoreRepository.albumsFlow
        .map { songs ->
            songs.asSequence().sortedBy { it.name }.toImmutableList()
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val artistsSharedFlow = mediaStoreRepository.artistsFlow
        .map { songs ->
            songs.asSequence().sortedBy { it.name }.toImmutableList()
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val songsSharedFlow = mediaStoreRepository.songsFlow
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val songsLittleSharedFlow = mediaStoreRepository.songsFlow
        .map { it.take(10).toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val favoriteSharedFlow = databaseRepository.favoriteFlow
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val historySharedFlow = databaseRepository.historyFlow
        .map { songs ->
            songs.asSequence().distinctBy { it.tag.mediaId }.toImmutableList()
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val playlistsSharedFlow = databaseRepository.playlistsFlow
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val topAlbumsSharedFlow = databaseRepository.topAlbumsFlow
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val topArtistsSharedFlow = databaseRepository.topArtistsFlow
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
    val topSongsSharedFlow = databaseRepository.topSongsFlow
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    val albumSongsSharedFlow = albumSongsSharedFlow(albumId)
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    val artistSongsSharedFlow = artistSongsSharedFlow(name)
        .map { it.toImmutableList() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    private fun albumSongsSharedFlow(albumId: Long): SharedFlow<ImmutableList<Library.Song>> {
        return mediaStoreRepository.albumSongsFlow(albumId)
            .map { it.toImmutableList() }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )
    }

    private fun artistSongsSharedFlow(artist: String): SharedFlow<ImmutableList<Library.Song>> {
        return mediaStoreRepository.artistsSongsFlow(artist)
            .map { it.toImmutableList() }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )
    }

    private val onShuffledSongs: MutableStateFlow<Unit> = MutableStateFlow(value = Unit)

    fun shuffleSongs() {
        onShuffledSongs.value = Unit
    }

    val shuffledSongsSharedFlow: SharedFlow<ImmutableList<Library.Song>> = combine(
        songsSharedFlow,
        onShuffledSongs,
    ) { songs, _ ->
        songs.asSequence().shuffled().toImmutableList()
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    fun like(songs: List<Library.Song>) {
        viewModelScope.launch {
            databaseRepository.setFavorite(songs.map { it.tag.mediaId })
        }
    }

    fun isFavoriteSharedFlow(song: Library.Song): SharedFlow<Boolean> {
        return databaseRepository.isFavoriteFlow(song.tag.mediaId)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )
    }
}
