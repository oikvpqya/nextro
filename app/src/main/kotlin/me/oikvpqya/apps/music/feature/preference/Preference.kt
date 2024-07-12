package me.oikvpqya.apps.music.feature.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.oikvpqya.apps.music.feature.Destination
import me.oikvpqya.apps.music.feature.RootRouteFactory
import me.tatarka.inject.annotations.Inject

@Inject
class PreferenceRouteFactory : RootRouteFactory {
    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {
        composable<Destination.Preference> { _ ->
            PreferenceRoute(navController, modifier)
        }
    }
}

@Composable
internal fun PreferenceRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
}
