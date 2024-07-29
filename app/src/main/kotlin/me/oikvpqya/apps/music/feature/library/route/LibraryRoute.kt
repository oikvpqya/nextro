package me.oikvpqya.apps.music.feature.library.route

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import me.oikvpqya.apps.music.feature.library.screen.LibrariesDetailScreen
import me.oikvpqya.apps.music.feature.library.screen.LibrariesGridScreen
import me.oikvpqya.apps.music.feature.library.screen.SongsScreen
import me.oikvpqya.apps.music.feature.library.screen.SuggestionsScreen
import me.oikvpqya.apps.music.feature.library.screen.TopPlayingScreen
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Tag
import me.oikvpqya.apps.music.model.asPlaylistTag
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.component.icons.History
import me.oikvpqya.apps.music.ui.component.icons.TrendingUp
import me.oikvpqya.apps.music.ui.composable.AppDragHandle

@Composable
fun AlbumsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val albums by viewModel.albumsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "Albums",
        )
        LibrariesGridScreen(
            modifier = modifier,
            libraries = albums,
            onItemClick = { libraries ->
                val destination = with(libraries) {
                    when (this) {
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
}

@Composable
fun ArtistsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val artists by viewModel.artistsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "Artists",
        )
        LibrariesGridScreen(
            modifier = modifier,
            libraries = artists,
            onItemClick = { libraries ->
                val destination = with(libraries) {
                    when (this) {
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
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "Playlists",
        )
        LibrariesGridScreen(
            modifier = modifier,
            libraries = items,
            onItemClick = { libraries ->
                val destination = with(libraries) {
                    when (this) {
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
}

@Composable
fun SongsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.songsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "Songs",
        )
        SongsScreen(
            modifier = modifier,
            items = songs,
            onItemClick = { index ->
                mediaHandler?.playSongs(items = songs, index = index)
            },
        )
    }
}

@Composable
fun SuggestionsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val shuffledSongs by viewModel.shuffledSongsSharedFlow
        .map { it.take(8).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "Suggestions",
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.Suggestions)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.ForYou,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.Histories)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.History,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.TopPlaying)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.TrendingUp,
                        contentDescription = null,
                    )
                }
            },
        )
        SuggestionsScreen(
            modifier = Modifier.fillMaxSize(),
            suggestionSongs = shuffledSongs,
        )
    }
}

@Composable
fun TopPlayingRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val topSongs by viewModel.topSongsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val topAlbums by viewModel.topAlbumsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val topArtists by viewModel.topArtistsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "TopPlaying",
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.Suggestions)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.ForYou,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.Histories)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.History,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.TopPlaying)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.TrendingUp,
                        contentDescription = null,
                    )
                }
            },
        )
        TopPlayingScreen(
            modifier = Modifier.fillMaxSize(),
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
}

@Composable
fun HistoriesRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val histories by viewModel.historySharedFlow
        .map { it.toImmutableList() }
        .collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            text = "Histories",
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.Suggestions)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.ForYou,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.Histories)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.History,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(HomeDestination.TopPlaying)
                    }
                ) {
                    Icon(
                        imageVector = AppIcons.TrendingUp,
                        contentDescription = null,
                    )
                }
            },
        )
        SongsScreen(
            modifier = modifier,
            items = histories,
            onItemClick = { index ->
                mediaHandler?.playSongs(items = histories, index = index)
            },
        )
    }
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
