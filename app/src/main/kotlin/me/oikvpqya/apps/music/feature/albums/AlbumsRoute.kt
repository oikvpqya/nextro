package me.oikvpqya.apps.music.feature.albums

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
import me.oikvpqya.apps.music.feature.AlbumsDestination
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.library.route.AlbumDetailRoute
import me.oikvpqya.apps.music.feature.library.screen.LibrariesGridScreen
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.feature.startDestination
import me.tatarka.inject.annotations.Inject

@Inject
class AlbumsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        navigation<MainDestination.Albums>(
            startDestination = MainDestination.Albums.startDestination,
        ) {
            composable<AlbumsDestination.Albums> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumsRoute(navController, viewModel, modifier)
            }
            composable<AlbumsDestination.Detail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                AlbumDetailRoute(navController, viewModel, modifier)
            }
        }
    }
}

@Composable
fun AlbumsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val albums by viewModel.albumsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
//    val mediaHandler by LocalMediaHandlerState.current
    LibrariesGridScreen(
        modifier = modifier,
        libraries = albums,
        onItemClick = { libraries ->
            navController.navigate(
                with(libraries) { AlbumsDestination.Detail(name, summary, tag.albumId) }
            )
//            mediaHandler?.playSongs(songs, 0)
        }
    )
}
