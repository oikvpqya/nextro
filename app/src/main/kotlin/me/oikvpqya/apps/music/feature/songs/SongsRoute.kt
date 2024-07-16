package me.oikvpqya.apps.music.feature.songs

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
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.SongsDestination
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.feature.startDestination
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.tatarka.inject.annotations.Inject

@Inject
class SongsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        navigation<MainDestination.Songs>(
            startDestination = MainDestination.Songs.startDestination,
        ) {
            composable<SongsDestination.Songs> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                SongsRoute(navController, viewModel, modifier)
            }
        }
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
    SongsScreen(
        modifier = modifier,
        items = songs,
        onItemClick = { index ->
            mediaHandler?.playSongs(items = songs, index = index)
        },
    )
}
