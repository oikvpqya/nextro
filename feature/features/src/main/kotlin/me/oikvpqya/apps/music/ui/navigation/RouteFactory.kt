package me.oikvpqya.apps.music.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface RouteFactory {

    fun NavGraphBuilder.create(
        navController: NavController,
        modifier: Modifier,
    )
}

fun NavGraphBuilder.create(
    factories: Set<RouteFactory>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    factories.forEach { factory ->
        with(factory) {
            this@create.create(
                navController = navController,
                modifier = modifier,
            )
        }
    }
}
