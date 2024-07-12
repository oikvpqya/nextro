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

fun RouteFactory.create(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder,
    modifier: Modifier = Modifier,
) = navGraphBuilder.create(navController, modifier)

fun NavGraphBuilder.create(
    factory: RouteFactory,
    navController: NavController,
    modifier: Modifier = Modifier,
) = factory.create(navController, this, modifier)

fun NavGraphBuilder.create(
    factories: Set<RouteFactory>,
    navController: NavController,
    modifier: Modifier = Modifier,
) = factories.forEach { create(it, navController, modifier) }
