package me.oikvpqya.apps.music.feature.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import me.oikvpqya.apps.music.feature.AppDestination
import me.oikvpqya.apps.music.feature.MainOverlay
import me.oikvpqya.apps.music.ui.util.LocalAppSnackbarHandler
import me.oikvpqya.apps.music.feature.main.component.MainBottomBar
import me.oikvpqya.apps.music.feature.player.component.ExpandingPlayerContainer
import me.oikvpqya.apps.music.feature.startDestination
import me.oikvpqya.apps.music.ui.component.CollapsingPlayerContainer
import me.oikvpqya.apps.music.ui.media.LocalMediaHandlerState
import me.oikvpqya.apps.music.ui.media.LocalMediaInfoState
import me.oikvpqya.apps.music.ui.navigation.SheetRouteFactory
import me.oikvpqya.apps.music.ui.navigation.create
import me.oikvpqya.apps.music.ui.util.APP_BAR_CONTAINER_HEIGHT
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_CONTAINER_HEIGHT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isCollapsingPlayer: Boolean,
    setCollapsingPlayerFlag: (Boolean) -> Unit,
    routeFactories: Set<SheetRouteFactory>,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    val modalBottomSheetNavigator = rememberModalBottomSheetNavigator(sheetState)
    val navController = rememberNavController(
        modalBottomSheetNavigator
    )
    val snackbarHostState = LocalAppSnackbarHandler.current.state
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState,
        snackbarHostState = snackbarHostState,
    )

    Scaffold(
        modifier = modifier,
        bottomBar = { MainScreenBottomBar(navController = navController) },
    ) { topInnerPadding ->
        Box(
            modifier = Modifier
                .padding(topInnerPadding),
        ) {
            BottomSheetScaffold(
                sheetSwipeEnabled = false,
                scaffoldState = scaffoldState,
                sheetPeekHeight = 128.dp,
                sheetContent = {
                    MainScreenSheetContent(
                        navController = navController,
                        routeFactories = routeFactories,
                        sheetState = bottomSheetState,
                    )
                },
                sheetDragHandle = {},
                content = { bottomSheetInnerPadding ->
                    MainScreenMainContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottomSheetInnerPadding),
                        sheetState = bottomSheetState,
                        onMoreClick = {
                            navController.navigate(route = MainOverlay.PlayerModalBottomSheet)
                        },
                    )
                },
            )
        }
    }

    val isModalBottomSheetNavigator = navController.navigatorProvider
        .get<Navigator<out NavDestination>>(ModalBottomSheetNavigator.NAME)
            as? ModalBottomSheetNavigator != null
    if (isModalBottomSheetNavigator) {
        ModalBottomSheetHost(
            modalBottomSheetNavigator = modalBottomSheetNavigator,
            sheetState = sheetState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenSheetContent(
    navController: NavHostController,
    sheetState: SheetState,
    routeFactories: Set<SheetRouteFactory>,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier
            .layout { measurable, constraints ->
                val topBarHeight = APP_BAR_CONTAINER_HEIGHT.roundToPx()
                val collapsingPlayerHeight = LIST_TRACK_CONTAINER_HEIGHT.roundToPx()
                val nonHiddenHeight = topBarHeight + collapsingPlayerHeight
                val layoutWidth = constraints.maxWidth
                val layoutHeight = (constraints.maxHeight - nonHiddenHeight).coerceAtLeast(0)
                val placeable = measurable.measure(
                    constraints.copy(
                        maxHeight = layoutHeight,
                    )
                )
                layout(
                    width = layoutWidth,
                    height = layoutHeight,
                ) {
                    placeable.placeRelative(
                        x = 0,
                        y = 0,
                    )
                }
            },
        navController = navController,
        startDestination = AppDestination.Main.startDestination,
    ) {
        create(
            factories = routeFactories,
            navController = navController,
            sheetState = sheetState,
        )
        modalBottomSheet<MainOverlay.InfoModalBottomSheet> {
            // TODO
        }
        modalBottomSheet<MainOverlay.PlayerModalBottomSheet> {
            // Volume Slider
            val mediaInfo by LocalMediaInfoState.current
            val mediaHandler by LocalMediaHandlerState.current
            val volume = mediaInfo.volume
            var sliderValue by remember { mutableFloatStateOf(volume) }
            var onSliding by remember { mutableStateOf(false) }
            val animateProgress by animateFloatAsState(
                targetValue = if (onSliding) { sliderValue } else volume,
                label = "",
                animationSpec = tween(durationMillis = if (onSliding) 0 else 1000)
            )
            Slider(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = animateProgress,
                onValueChange = { value ->
                    onSliding = true
                    sliderValue = value
                    mediaHandler?.setVolume(sliderValue)
                },
                onValueChangeFinished = {
                    onSliding = false
                },
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
private fun MainScreenMainContent(
    sheetState: SheetState,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nonHiddenHeight = with(LocalDensity.current) {
        val topBarHeight = APP_BAR_CONTAINER_HEIGHT.roundToPx()
        val collapsingPlayerHeight = LIST_TRACK_CONTAINER_HEIGHT.roundToPx()
        topBarHeight + collapsingPlayerHeight
    }
    val triggerPadding = with(LocalDensity.current) {
        48.dp.roundToPx()
    }

    val isFullyExpanded: Boolean by remember {
        derivedStateOf {
            try {
                val offset = sheetState.requireOffset()
//                val isTop = offset - nonHiddenHeight == 0f
                offset - nonHiddenHeight < triggerPadding
            } catch (e: IllegalStateException) {
                true
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        MainScreenTopBar()
        SharedTransitionLayout {
            AnimatedContent(
                targetState = isFullyExpanded,
                label = ""
            ) { targetState ->
                if (targetState) {
                    CollapsingPlayerContainer(
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout,
                    )
                } else {
                    ExpandingPlayerContainer(
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        onMoreClick = onMoreClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreenBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        content = { MainBottomBar(navController = navController) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "ウナのおうた♪🥺") },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}
