package me.oikvpqya.apps.music.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.oikvpqya.apps.music.feature.Destination
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.RootRouteFactory
import me.oikvpqya.apps.music.ui.navigation.RouteFactory
import me.tatarka.inject.annotations.Inject

@Inject
class MainRouteFactory(
    private val routeFactories: Set<LibraryRouteFactory>,
    private val viewModelFactory: () -> MainViewModel,
) : RootRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        composable<Destination.Main> { _ ->
            val viewModel = viewModel { viewModelFactory() }
            MainRoute(navController, routeFactories, viewModel, modifier)
        }
    }
}

@Composable
fun MainRoute(
    navController: NavController,
    routeFactories: Set<RouteFactory>,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    MainScreen(
        modifier = modifier,
        isCollapsingPlayer = uiState.isCollapsingPlayer,
        setCollapsingPlayerFlag = { viewModel.setCollapsingPlayerFlag(it) },
        routeFactories = routeFactories,
    )
}
