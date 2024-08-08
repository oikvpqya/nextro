package me.oikvpqya.apps.music.feature.library.route

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.oikvpqya.apps.music.feature.AlbumsDestination
import me.oikvpqya.apps.music.feature.ArtistsDestination
import me.oikvpqya.apps.music.feature.Destination
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
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag
import me.oikvpqya.apps.music.model.asPlaylistTag
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.component.icons.History
import me.oikvpqya.apps.music.ui.component.icons.TrendingUp
import me.oikvpqya.apps.music.ui.composable.AppDragHandle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val albums by viewModel.albumsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "Albums",
        )
        LibrariesGridScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            libraries = albums,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
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
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistsRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val artists by viewModel.artistsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "Artists",
        )
        LibrariesGridScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            libraries = artists,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
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
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsRoute(
    navController: NavController,
    sheetState: SheetState,
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
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "Playlists",
        )
        LibrariesGridScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            libraries = items,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
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
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.songsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    val mediaHandler by LocalMediaHandlerState.current
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "Songs",
        )
        SongsScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            items = songs,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
            onItemClick = { index ->
                mediaHandler?.playSongs(items = songs, index = index)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsRoute(
    navController: NavController,
    sheetState: SheetState,
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
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "Suggestions",
            actions = {
                SheetNavigationIcons(
                    navController = navController,
                    icons = homeIcons,
                )
            },
        )
        SuggestionsScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            suggestionSongs = shuffledSongs,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopPlayingRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val topSongs: ImmutableList<Library.Song>? by viewModel.topSongsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(null)
    val topAlbums: ImmutableList<Libraries.Album>? by viewModel.topAlbumsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(null)
    val topArtists: ImmutableList<Libraries.Default>? by viewModel.topArtistsSharedFlow
        .map { it.take(4).toImmutableList() }
        .collectAsStateWithLifecycle(null)
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "TopPlaying",
            actions = {
                SheetNavigationIcons(
                    navController = navController,
                    icons = homeIcons,
                )
            },
        )
        TopPlayingScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            topAlbums = topAlbums ?: return@Column,
            topArtists = topArtists ?: return@Column,
            topSongs = topSongs ?: return@Column,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
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
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoriesRoute(
    navController: NavController,
    sheetState: SheetState,
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
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "Histories",
            actions = {
                SheetNavigationIcons(
                    navController = navController,
                    icons = homeIcons,
                )
            },
        )
        SongsScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            items = histories,
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
            onItemClick = { index ->
                mediaHandler?.playSongs(items = histories, index = index)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.favoriteSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    DetailRoute(
        modifier = modifier,
        navController = navController,
        sheetState = sheetState,
        songs = songs,
        name = viewModel.name,
        summary = viewModel.summary,
        albumId = viewModel.albumId,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.artistSongsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    DetailRoute(
        modifier = modifier,
        navController = navController,
        sheetState = sheetState,
        songs = songs,
        name = viewModel.name,
        summary = viewModel.summary,
        albumId = viewModel.albumId,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailRoute(
    navController: NavController,
    sheetState: SheetState,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val songs by viewModel.albumSongsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    DetailRoute(
        modifier = modifier,
        navController = navController,
        sheetState = sheetState,
        songs = songs,
        name = viewModel.name,
        summary = viewModel.summary,
        albumId = viewModel.albumId,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRoute(
    navController: NavController,
    sheetState: SheetState,
    songs: ImmutableList<Library.Song>,
    name: String,
    summary: String,
    albumId: Long,
    modifier: Modifier = Modifier,
) {
    val mediaHandler by LocalMediaHandlerState.current

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AppDragHandle(
            modifier = Modifier.anchoredDraggable(sheetState, Orientation.Vertical),
            text = "",
            actions = {
                DetailSheetNavigationIcons(
                    navController = navController,
                )
            },
        )
        LibrariesDetailScreen(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    sheetState, Orientation.Vertical,
                    enabled = sheetState.currentValue != SheetValue.Expanded,
                ),
            name = name,
            summary = summary,
            albumId = albumId,
            songs = songs,
            firstItems = emptyList(),
            secondItems = emptyList(),
            isSheetExpanded = sheetState.currentValue == SheetValue.Expanded,
            onLibrariesClick = {},
            onSongClick = { index ->
                mediaHandler?.playSongs(songs, index)
            }
        )
    }
}

private data class SheetNavigationItem(
    val icon: ImageVector,
    val route: Destination,
)

@Composable
private fun RowScope.SheetNavigationIcons(
    navController: NavController,
    icons: ImmutableList<SheetNavigationItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    icons.forEach { item ->
        val selected = currentDestination?.hierarchy
            ?.any { it.hasRoute(item.route::class) }
            ?: false
        if (selected) {
            FilledTonalIconButton(
                modifier = Modifier.size(36.dp),
                onClick = {
//                    navController.navigate(item.route)
                }
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                )
            }
        } else {
            IconButton(
                modifier = Modifier.size(36.dp),
                onClick = {
                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun RowScope.DetailSheetNavigationIcons(
    navController: NavController,
) {
    FilledTonalIconButton(
        modifier = Modifier.size(36.dp),
        onClick = {
            navController.navigateUp()
        }
    ) {
        Icon(
            imageVector = AppIcons.Close,
            contentDescription = null,
        )
    }
}

private val homeIcons: ImmutableList<SheetNavigationItem> = persistentListOf(
    SheetNavigationItem(AppIcons.ForYou, HomeDestination.Suggestions),
    SheetNavigationItem(AppIcons.History, HomeDestination.Histories),
    SheetNavigationItem(AppIcons.TrendingUp, HomeDestination.TopPlaying),
)

@OptIn(ExperimentalMaterial3Api::class)
private fun Modifier.anchoredDraggable(
    sheetState: SheetState,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
): Modifier {
    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
    val draggableState = sheetState.anchoredDraggableState.draggableState
    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
    val isAnimationRunning = sheetState.anchoredDraggableState.isAnimationRunning
    suspend fun SheetState.settle(velocity: Float) {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        anchoredDraggableState.settle(velocity)
    }
    return draggable(
        state =  draggableState,
        orientation = orientation,
        enabled = enabled,
        interactionSource = interactionSource,
        reverseDirection = reverseDirection,
        startDragImmediately = isAnimationRunning,
        onDragStopped = { velocity -> launch { sheetState.settle(velocity) } }
    )
}
