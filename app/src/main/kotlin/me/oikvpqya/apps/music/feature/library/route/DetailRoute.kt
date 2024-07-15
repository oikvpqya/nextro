package me.oikvpqya.apps.music.feature.library.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.collections.immutable.persistentListOf
import me.oikvpqya.apps.music.feature.library.screen.LibrariesDetailScreen
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState

@Composable
fun PlaylistDetailRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.favoriteSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    LibrariesDetailScreen(
        modifier = modifier,
        name = viewModel.name,
        summary = viewModel.summary,
        albumId = viewModel.albumId,
        songs = songs,
        firstItems = emptyList(),
        secondItems = emptyList(),
        onLibrariesClick = {},
        onSongClick = { index ->
            mediaHandler?.playSongs(songs, index)
        }
    )
}

@Composable
fun ArtistDetailRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.artistSongsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    LibrariesDetailScreen(
        modifier = modifier,
        name = viewModel.name,
        summary = viewModel.summary,
        albumId = viewModel.albumId,
        songs = songs,
        firstItems = emptyList(),
        secondItems = emptyList(),
        onLibrariesClick = {},
        onSongClick = { index ->
            mediaHandler?.playSongs(songs, index)
        }
    )
}


@Composable
fun AlbumDetailRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.albumSongsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    LibrariesDetailScreen(
        modifier = modifier,
        name = viewModel.name,
        summary = viewModel.summary,
        albumId = viewModel.albumId,
        songs = songs,
        firstItems = emptyList(),
        secondItems = emptyList(),
        onLibrariesClick = {},
        onSongClick = { index ->
            mediaHandler?.playSongs(songs, index)
        }
    )
}