package me.oikvpqya.apps.music.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.ui.util.LocalAppSnackbarHandler
import me.oikvpqya.apps.music.feature.main.component.MainBottomBar
import me.oikvpqya.apps.music.feature.player.PlayerScreen
import me.oikvpqya.apps.music.feature.main.component.MainScaffold
import me.oikvpqya.apps.music.ui.component.CollapsingPlayerContainer
import me.oikvpqya.apps.music.ui.navigation.RouteFactory
import me.oikvpqya.apps.music.ui.navigation.create
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_CONTAINER_HEIGHT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isCollapsingPlayer: Boolean,
    setCollapsingPlayerFlag: (Boolean) -> Unit,
    routeFactories: Set<RouteFactory>,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val snackbarHostState = LocalAppSnackbarHandler.current.state

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    MainScaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        navController = navController,
        isCollapsingPlayer = isCollapsingPlayer,
        setCollapsingPlayerFlag = setCollapsingPlayerFlag,
        isCollapsingBottomBar = false,
        snackbar = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            MainBottomBar(
                navController = navController
            )
        },
        collapsedBottomSheet = {
            CollapsingPlayerContainer()
        },
        expandedBottomSheet = {
            PlayerScreen()
        },
        fab = {},
    ) {
        Column(
            modifier = modifier
        ) {
            CenterAlignedTopAppBar(
                title = { Text(text = "") },
                scrollBehavior = scrollBehavior
            )
            NavHost(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = LIST_TRACK_CONTAINER_HEIGHT),
                navController = navController,
                startDestination = MainDestination.Home,
            ) {
                create(
                    factories = routeFactories,
                    navController = navController,
                )
            }
        }
    }
}
