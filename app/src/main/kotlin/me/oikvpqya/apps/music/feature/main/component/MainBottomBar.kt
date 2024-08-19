package me.oikvpqya.apps.music.feature.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import me.oikvpqya.apps.music.feature.MainDestination
import me.oikvpqya.apps.music.feature.startDestination
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.util.BOTTOM_BAR_CONTAINER_HEIGHT

@Composable
fun MainBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    alwaysShowLabel: Boolean = false,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Row(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .defaultMinSize(minHeight = BOTTOM_BAR_CONTAINER_HEIGHT)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Items.forEach { item ->
            val selected = currentDestination?.hierarchy
                ?.any { it.hasRoute(item.route::class) }
                ?: false
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (selected) {
                        navController.popBackStack(
                            route = item.route.startDestination,
                            inclusive = false,
                        )
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(item.icon, contentDescription = null)
                },
                label = {
                    Text(item.title)
                },
                alwaysShowLabel = alwaysShowLabel,
            )
        }
    }
}

private data class Item(
    val title: String,
    val icon: ImageVector,
    val route: MainDestination,
)

private val Items = listOf(
    Item(
        "Home",
        AppIcons.ForYou,
        MainDestination.Home,
    ),
    Item(
        "Albums",
        AppIcons.Albums,
        MainDestination.Albums,
    ),
    Item(
        "Artists",
        AppIcons.Artists,
        MainDestination.Artists,
    ),
    Item(
        "Playlists",
        AppIcons.Playlists,
        MainDestination.Playlists,
    ),
    Item(
        "Songs",
        AppIcons.Song,
        MainDestination.Songs,
    )
)
