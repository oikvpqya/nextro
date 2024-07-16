package me.oikvpqya.apps.music.feature.main

import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.oikvpqya.apps.music.feature.AppDestination
import me.oikvpqya.apps.music.ui.util.LocalAppSnackbarHandler
import me.oikvpqya.apps.music.feature.main.component.MainBottomBar
import me.oikvpqya.apps.music.feature.player.PlayerScreen
import me.oikvpqya.apps.music.feature.main.component.MainScaffold
import me.oikvpqya.apps.music.feature.startDestination
import me.oikvpqya.apps.music.ui.component.CollapsingPlayerContainer
import me.oikvpqya.apps.music.ui.navigation.RouteFactory
import me.oikvpqya.apps.music.ui.navigation.create

@Composable
fun MainScreen(
    isCollapsingPlayer: Boolean,
    setCollapsingPlayerFlag: (Boolean) -> Unit,
    routeFactories: Set<RouteFactory>,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val snackbarHostState = LocalAppSnackbarHandler.current.state

    MainScaffold(
        modifier = modifier,
        isCollapsingPlayer = isCollapsingPlayer,
        setCollapsingPlayerFlag = setCollapsingPlayerFlag,
        isCollapsingBottomBar = false,
        snackbar = { SnackbarHost(snackbarHostState) },
        bottomBar = { MainBottomBar(navController = navController) },
        collapsedBottomSheet = { CollapsingPlayerContainer() },
        expandedBottomSheet = { PlayerScreen() },
        fab = {},
    ) {
        NavHost(
            navController = navController,
            startDestination = AppDestination.Main.startDestination,
        ) {
            create(
                factories = routeFactories,
                navController = navController,
            )
        }
    }
}
