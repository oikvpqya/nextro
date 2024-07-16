package me.oikvpqya.apps.music.feature.playlists

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
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.PlaylistsDestination
import me.oikvpqya.apps.music.feature.library.route.PlaylistDetailRoute
import me.oikvpqya.apps.music.feature.library.screen.LibrariesGridScreen
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.feature.startDestination
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Tag
import me.oikvpqya.apps.music.model.asPlaylistTag
import me.tatarka.inject.annotations.Inject

@Inject
class PlaylistsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        navigation<MainDestination.Playlists>(
            startDestination = MainDestination.Playlists.startDestination,
        ) {
            composable<PlaylistsDestination.Playlists> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistsRoute(navController, viewModel, modifier)
            }
            composable<PlaylistsDestination.Detail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                PlaylistDetailRoute(navController, viewModel, modifier)
            }
        }
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
    LibrariesGridScreen(
        modifier = modifier,
        libraries = items,
        onItemClick = { libraries ->
            navController.navigate(
                with(libraries) { PlaylistsDestination.Detail(name, summary, tag.albumId) }
            )
        }
    )
}
