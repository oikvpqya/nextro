package me.oikvpqya.apps.music.feature.library.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import me.oikvpqya.apps.music.feature.AlbumsDestination
import me.oikvpqya.apps.music.feature.ArtistsDestination
import me.oikvpqya.apps.music.feature.HomeDestination
import me.oikvpqya.apps.music.feature.PlaylistsDestination
import me.oikvpqya.apps.music.feature.library.screen.HomeScreen
import me.oikvpqya.apps.music.feature.library.screen.LibrariesDetailScreen
import me.oikvpqya.apps.music.feature.library.screen.LibrariesGridScreen
import me.oikvpqya.apps.music.feature.library.screen.SongsScreen
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Tag
import me.oikvpqya.apps.music.model.asPlaylistTag

@Composable
fun AlbumsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val albums by viewModel.albumsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    LibrariesGridScreen(
        modifier = modifier,
        libraries = albums,
        onItemClick = { libraries ->
            val destination = with(libraries) {
                when(this) {
                    is Libraries.Album -> {
                        AlbumsDestination.AlbumDetail(name, summary, tag.albumId)
                    }
                    is Libraries.Default -> {
                        AlbumsDestination.ArtistDetail(name, summary, tag.albumId)
                    }
                    is Libraries.Playlist -> {
                        AlbumsDestination.PlaylistDetail(name, summary, tag.albumId)
                    }
                }
            }
            navController.navigate(destination)
        }
    )
}

@Composable
fun ArtistsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val artists by viewModel.artistsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    LibrariesGridScreen(
        modifier = modifier,
        libraries = artists,
        onItemClick = { libraries ->
            val destination = with(libraries) {
                when(this) {
                    is Libraries.Album -> {
                        ArtistsDestination.AlbumDetail(name, summary, tag.albumId)
                    }
                    is Libraries.Default -> {
                        ArtistsDestination.ArtistDetail(name, summary, tag.albumId)
                    }
                    is Libraries.Playlist -> {
                        ArtistsDestination.PlaylistDetail(name, summary, tag.albumId)
                    }
                }
            }
            navController.navigate(destination)
        }
    )
}

@Composable
fun PlaylistsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val playlists by viewModel.playlistsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val favorite by viewModel.favoriteSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val favoriteName = "Favorite"
    val favoritePlaylist = Libraries.Playlist(
        tag = favorite.firstOrNull()?.tag?.asPlaylistTag(favoriteName)
            ?: Tag.Default(albumId = -1L, name = favoriteName),
        size = favorite.size
    )
    val items = ((playlists + favoritePlaylist).sortedBy { it.name }).toImmutableList()
    LibrariesGridScreen(
        modifier = modifier,
        libraries = items,
        onItemClick = { libraries ->
            val destination = with(libraries) {
                when(this) {
                    is Libraries.Album -> {
                        PlaylistsDestination.AlbumDetail(name, summary, tag.albumId)
                    }
                    is Libraries.Default -> {
                        PlaylistsDestination.ArtistDetail(name, summary, tag.albumId)
                    }
                    is Libraries.Playlist -> {
                        PlaylistsDestination.PlaylistDetail(name, summary, tag.albumId)
                    }
                }
            }
            navController.navigate(destination)
        }
    )
}

@Composable
fun SongsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.songsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    SongsScreen(
        modifier = modifier,
        items = songs,
        onItemClick = { index ->
            mediaHandler?.playSongs(items = songs, index = index)
        },
    )
}

@Composable
fun HomeRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val favoriteSongs by viewModel.favoriteSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val historySongs by viewModel.historySharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val shuffledSongs by viewModel.shuffledSongsSharedFlow
        .map { it.take(8).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val topSongs by viewModel.topSongsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val topAlbums by viewModel.topAlbumsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val topArtists by viewModel.topArtistsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())

    HomeScreen(
        modifier = modifier,
        favoriteSongs = favoriteSongs,
        historySongs = historySongs,
        suggestionSongs = shuffledSongs,
        topAlbums = topAlbums,
        topArtists = topArtists,
        topSongs = topSongs,
        onLibrariesClick = { libraries ->
            with(libraries) {
                when (this) {
                    is Libraries.Album -> navController.navigate(
                        HomeDestination.AlbumDetail(name, summary, tag.albumId)
                    )

                    is Libraries.Default -> navController.navigate(
                        HomeDestination.ArtistDetail(name, summary, tag.albumId)
                    )

                    is Libraries.Playlist -> navController.navigate(
                        HomeDestination.PlaylistDetail(name, summary, tag.albumId)
                    )
                }
            }
        }
    )
}


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
