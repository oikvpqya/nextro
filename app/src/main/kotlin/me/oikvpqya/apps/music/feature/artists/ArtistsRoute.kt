package me.oikvpqya.apps.music.feature.artists

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
import me.oikvpqya.apps.music.feature.ArtistsDestination
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.library.route.ArtistDetailRoute
import me.oikvpqya.apps.music.feature.library.screen.LibrariesGridScreen
import me.oikvpqya.apps.music.feature.library.viewmodel.LibraryViewModel
import me.oikvpqya.apps.music.feature.main.component.TopBarScreen
import me.oikvpqya.apps.music.feature.startDestination
import me.tatarka.inject.annotations.Inject

@Inject
class ArtistsRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> LibraryViewModel,
) : LibraryRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        navigation<MainDestination.Artists>(
            startDestination = MainDestination.Artists.startDestination,
        ) {
            composable<ArtistsDestination.Artists> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistsRoute(navController, viewModel, modifier)
            }
            composable<ArtistsDestination.Detail> { _ ->
                val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
                ArtistDetailRoute(navController, viewModel, modifier)
            }
        }
    }
}

@Composable
fun ArtistsRoute(
    navController: NavController,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val artists by viewModel.artistsSharedFlow.collectAsStateWithLifecycle(persistentListOf())
    TopBarScreen(
        modifier = modifier,
        title = "Artists",
    ) {
        LibrariesGridScreen(
            libraries = artists,
            onItemClick = { libraries ->
                navController.navigate(
                    with(libraries) { ArtistsDestination.Detail(name, summary, tag.albumId) }
                )
            }
        )
    }
}
