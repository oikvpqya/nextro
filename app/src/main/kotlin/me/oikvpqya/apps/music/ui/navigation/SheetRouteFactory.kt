package me.oikvpqya.apps.music.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface SheetRouteFactory {

    @OptIn(ExperimentalMaterial3Api::class)
    fun NavGraphBuilder.create(
        navController: NavController,
        sheetState: SheetState,
        modifier: Modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.create(
    factories: Set<SheetRouteFactory>,
    navController: NavController,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
) {
    factories.forEach { factory ->
        with(factory) {
            this@create.create(
                navController = navController,
                sheetState = sheetState,
                modifier = modifier,
            )
        }
    }
}
