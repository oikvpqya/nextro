package me.oikvpqya.apps.music.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import me.oikvpqya.apps.music.feature.HomeDestination
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.library.route.AlbumDetailRoute
import me.oikvpqya.apps.music.feature.library.route.ArtistDetailRoute
import me.oikvpqya.apps.music.feature.library.route.PlaylistDetailRoute
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.model.Libraries
import me.tatarka.inject.annotations.Inject

@Inject
class HomeRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        navigation<MainDestination.Home>(
            startDestination = HomeDestination.Home,
        ) {
            composable<HomeDestination.Home> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                HomeRoute(navController, viewModel, modifier)
            }
            composable<HomeDestination.AlbumDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, viewModel, modifier)
            }
            composable<HomeDestination.ArtistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, viewModel, modifier)
            }
            composable<HomeDestination.PlaylistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, viewModel, modifier)
            }
        }
    }
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
        favoriteSongs = favoriteSongs,
        historySongs = historySongs,
        suggestionSongs = shuffledSongs,
        topAlbums = topAlbums,
        topArtists = topArtists,
        topSongs = topSongs,
        modifier = modifier,
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
