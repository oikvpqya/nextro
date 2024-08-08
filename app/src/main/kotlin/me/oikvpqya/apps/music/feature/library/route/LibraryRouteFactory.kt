package me.oikvpqya.apps.music.feature.library.route

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.oikvpqya.apps.music.feature.AlbumsDestination
import me.oikvpqya.apps.music.feature.ArtistsDestination
import me.oikvpqya.apps.music.feature.HomeDestination
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.PlaylistsDestination
import me.oikvpqya.apps.music.feature.SongsDestination
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.feature.startDestination
import me.oikvpqya.apps.music.ui.navigation.SheetRouteFactory
import me.tatarka.inject.annotations.Inject

interface LibraryRouteFactory : SheetRouteFactory
@Inject
class AlbumsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.create(
        navController: NavController,
        sheetState: SheetState,
        modifier: Modifier,
    ) {
        navigation<MainDestination.Albums>(
            startDestination = MainDestination.Albums.startDestination,
        ) {
            composable<AlbumsDestination.Albums> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumsRoute(navController, sheetState, viewModel, modifier)
            }
            composable<AlbumsDestination.AlbumDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<AlbumsDestination.ArtistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<AlbumsDestination.PlaylistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, sheetState, viewModel, modifier)
            }
        }
    }
}

@Inject
class ArtistsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.create(
        navController: NavController,
        sheetState: SheetState,
        modifier: Modifier,
    ) {
        navigation<MainDestination.Artists>(
            startDestination = MainDestination.Artists.startDestination,
        ) {
            composable<ArtistsDestination.Artists> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistsRoute(navController, sheetState, viewModel, modifier)
            }
            composable<ArtistsDestination.AlbumDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<ArtistsDestination.ArtistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<ArtistsDestination.PlaylistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, sheetState, viewModel, modifier)
            }
        }
    }
}

@Inject
class HomeRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.create(
        navController: NavController,
        sheetState: SheetState,
        modifier: Modifier,
    ) {
        navigation<MainDestination.Home>(
            startDestination = MainDestination.Home.startDestination,
        ) {
            composable<HomeDestination.Suggestions> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                SuggestionsRoute(navController, sheetState, viewModel, modifier)
            }
            composable<HomeDestination.TopPlaying> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                TopPlayingRoute(navController, sheetState, viewModel, modifier)
            }
            composable<HomeDestination.Histories> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                HistoriesRoute(navController, sheetState, viewModel, modifier)
            }
            composable<HomeDestination.AlbumDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<HomeDestination.ArtistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<HomeDestination.PlaylistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, sheetState, viewModel, modifier)
            }
        }
    }
}

@Inject
class PlaylistsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.create(
        navController: NavController,
        sheetState: SheetState,
        modifier: Modifier,
    ) {
        navigation<MainDestination.Playlists>(
            startDestination = MainDestination.Playlists.startDestination,
        ) {
            composable<PlaylistsDestination.Playlists> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistsRoute(navController, sheetState, viewModel, modifier)
            }
            composable<PlaylistsDestination.AlbumDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<PlaylistsDestination.ArtistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<PlaylistsDestination.PlaylistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, sheetState, viewModel, modifier)
            }
        }
    }
}

@Inject
class SongsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.create(
        navController: NavController,
        sheetState: SheetState,
        modifier: Modifier,
    ) {
        navigation<MainDestination.Songs>(
            startDestination = MainDestination.Songs.startDestination,
        ) {
            composable<SongsDestination.Songs> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                SongsRoute(navController, sheetState, viewModel, modifier)
            }
            composable<SongsDestination.AlbumDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<SongsDestination.ArtistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, sheetState, viewModel, modifier)
            }
            composable<SongsDestination.PlaylistDetail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, sheetState, viewModel, modifier)
            }
        }
    }
}
