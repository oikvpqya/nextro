package me.oikvpqya.apps.music.feature.root

import android.Manifest
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import me.oikvpqya.apps.music.data.MediaSynchronizer
import me.oikvpqya.apps.music.feature.AppDestination
import me.oikvpqya.apps.music.feature.RootRouteFactory
import me.oikvpqya.apps.music.feature.permission.PermissionRoute
import me.oikvpqya.apps.music.data.AppMediaController
import me.oikvpqya.apps.music.data.AppMediaInfo
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.media3.compose.LocalMediaInfoState
import me.oikvpqya.apps.music.ui.navigation.RouteFactory
import me.oikvpqya.apps.music.ui.navigation.create
import me.oikvpqya.apps.music.ui.theme.AppBackground
import me.oikvpqya.apps.music.ui.theme.AppTheme
import me.oikvpqya.apps.music.ui.util.AppSnackbarHandler
import me.oikvpqya.apps.music.ui.util.LocalAppSnackbarHandler
import me.tatarka.inject.annotations.Inject

interface AppContent {

    @Composable
    fun Content(
        modifier: Modifier,
    )
}

@Inject
class AppContentImpl(
    private val mediaSynchronizer: Lazy<MediaSynchronizer>,
    private val mediaController: Lazy<AppMediaController>,
    private val rootRouteFactories: Set<RootRouteFactory>,
) : AppContent {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun Content(modifier: Modifier) {
//        val windowSizeClass = LocalWindowSizeClass.current
        val snackbarHandler = remember { AppSnackbarHandler(SnackbarHostState()) }
        val readStoragePermissionState = rememberPermissionState(
            if (Build.VERSION.SDK_INT < 33) {
                Manifest.permission.READ_EXTERNAL_STORAGE
            } else {
                Manifest.permission.READ_MEDIA_AUDIO
            }
        )

        CompositionLocalProvider(
            LocalAppSnackbarHandler provides snackbarHandler,
//            LocalWindowSizeClass provides windowSizeClass,
        ) {
            val isDarkTheme = isSystemInDarkTheme()
            AppTheme(isDarkTheme) {
                AppBackground {
                    if (readStoragePermissionState.status.isGranted) {
                        AppRootRoute(
                            modifier = Modifier.fillMaxSize(),
                            mediaController = mediaController.value,
                            mediaSynchronizer = mediaSynchronizer.value,
                            factories = rootRouteFactories,
                        )
                    } else {
                        PermissionRoute(
                            modifier = Modifier.fillMaxSize(),
                            readStoragePermissionState = readStoragePermissionState,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppRootRoute(
    mediaController: AppMediaController,
    mediaSynchronizer: MediaSynchronizer,
    factories: Set<RouteFactory>,
    modifier: Modifier = Modifier,
) {
    mediaSynchronizer.startSync()
    CompositionLocalProvider(
        LocalMediaHandlerState provides mediaController.mediaHandlerFlow.collectAsStateWithLifecycle(
            initialValue = null,
            minActiveState = Lifecycle.State.RESUMED
        ),
        LocalMediaInfoState provides mediaController.mediaInfoFlow.collectAsStateWithLifecycle(
            initialValue = AppMediaInfo.Idle,
            minActiveState = Lifecycle.State.RESUMED
        ),
    ) {
        AppRoot(
            factories = factories,
            modifier = modifier,
        )
    }
}

@Composable
fun AppRoot(
    factories: Set<RouteFactory>,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppDestination.Main,
    ) {
        create(
            factories =  factories,
            navController = navController,
        )
    }
}
