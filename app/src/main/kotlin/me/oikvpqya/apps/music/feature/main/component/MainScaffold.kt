package me.oikvpqya.apps.music.feature.main.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirst
import kotlinx.coroutines.launch
import me.oikvpqya.apps.music.ui.util.BOTTOM_BAR_CONTAINER_HEIGHT
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_CONTAINER_HEIGHT
import kotlin.math.pow
import kotlin.math.roundToInt

enum class BottomSheetDragValue {
    COLLAPSED, EXPANDED, DISMISSED
}

private val FAB_PADDING = 16.dp

private const val ContentLayoutIdTag = "content"
private const val BottomBarLayoutIdTag = "bottomBar"
private const val BottomSheetLayoutIdTag = "bottomSheet"
private const val FabLayoutIdTag = "fab"
private const val SnackbarLayoutIdTag = "snackbar"

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun MainScaffold(
    bottomBar: @Composable () -> Unit,
    collapsedBottomSheet: @Composable () -> Unit,
    expandedBottomSheet: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    snackbar: @Composable () -> Unit,
    isCollapsingPlayer: Boolean,
    setCollapsingPlayerFlag: (Boolean) -> Unit,
    isCollapsingBottomBar: Boolean,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars,
    content: @Composable () -> Unit = {},
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
        .also {
            it.state.heightOffsetLimit = with(density) { -(BOTTOM_BAR_CONTAINER_HEIGHT.toPx()) }
        }

    val initialBottomSheetDragValue =
        if (isCollapsingPlayer) BottomSheetDragValue.COLLAPSED else BottomSheetDragValue.EXPANDED
    val animationSpec = SpringSpec<Float>()
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val anchoredDraggableState = with(LocalDensity.current) {
        remember {
            AnchoredDraggableState(
                initialValue = initialBottomSheetDragValue,
                positionalThreshold = { 56.dp.toPx() },
                velocityThreshold = { 125.dp.toPx() },
                snapAnimationSpec = animationSpec,
                decayAnimationSpec = decayAnimationSpec,
            )
        }
    }

    val bottomSheetOffset by remember {
        derivedStateOf {
            anchoredDraggableState.offset.roundToInt()
        }
    }

    val bottomSheetValue by remember {
        derivedStateOf {
            anchoredDraggableState.currentValue
        }
    }
    LaunchedEffect(bottomSheetValue) {
        setCollapsingPlayerFlag(bottomSheetValue == BottomSheetDragValue.COLLAPSED)
    }

    val bottomSheetIsInProgress: Float by remember {
        derivedStateOf {
            with(anchoredDraggableState) {
                progress(settledValue, targetValue)
            }
        }
    }
    LaunchedEffect(bottomSheetIsInProgress) {
        AnimationState(initialValue = scrollBehavior.state.heightOffset)
            .animateTo(
                targetValue = 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) { scrollBehavior.state.heightOffset = value }
    }

    val bottomBarOffsetProgress: Float by animateFloatAsState(
        targetValue = if (isCollapsingBottomBar) 0f else 1f,
        animationSpec = animationSpec,
        label = "bottomBarOffsetProgress"
    )
    val bottomBarAlphaProgress: Float by animateFloatAsState(
        targetValue = if (isCollapsingBottomBar) 0f else 1f,
        animationSpec = animationSpec,
        label = "bottomBarAlphaProgress"
    )

    val progress by remember {
        derivedStateOf {
            val (offset, track) = with(anchoredDraggableState) {
                offset to anchors.maxAnchor()
            }
            if (offset.isNaN() || track == Float.POSITIVE_INFINITY) {
                when (initialBottomSheetDragValue) {
                    BottomSheetDragValue.COLLAPSED -> 0f
                    BottomSheetDragValue.EXPANDED -> 1f
                    BottomSheetDragValue.DISMISSED -> error("")
                }
            }
            else {
                (1f - offset / track).coerceIn(0f..1f)
            }
        }
    }

    Layout(
        modifier = modifier,
        content = {
            Surface(
                modifier = Modifier
                    .alpha((1f - (progress * 4).coerceAtMost(1f)) * bottomBarAlphaProgress)
                    .layoutId(BottomBarLayoutIdTag),
                contentColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) { bottomBar() }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
//                    .clip(
//                        RoundedCornerShape(
//                            topStart = 24.dp * (1f - progress),
//                            topEnd = 24.dp * (1f - progress)
//                        )
//                    )
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        orientation = Orientation.Vertical,
                    )
                    .layoutId(BottomSheetLayoutIdTag),
                contentColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
            ) {
                if (progress != 1f) {
                    Box(
                        modifier = Modifier
                            .alpha(1f - (progress * 4).coerceIn(0f..1f))
                            .pointerInput(Unit) {
                                if (progress == 0f) {
                                    detectTapGestures {
                                        coroutineScope.launch {
                                            anchoredDraggableState.animateTo(BottomSheetDragValue.EXPANDED)
                                        }
                                    }
                                }
                            }
                    ) { collapsedBottomSheet() }
                }
                if (progress != 0f) {
                    Box(
                        modifier = Modifier.alpha(((progress - 0.25f) * 4).coerceIn(0f..1f))
                    ) { expandedBottomSheet() }
                }
            }
            Box(
                modifier = Modifier.layoutId(FabLayoutIdTag)
            ) { fab() }
            Box(
                modifier = Modifier.layoutId(SnackbarLayoutIdTag)
            ) { snackbar() }
            Box(
                modifier = Modifier
//                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .layoutId(ContentLayoutIdTag)
            ) { content() }
            BackHandler(bottomSheetValue == BottomSheetDragValue.EXPANDED) {
                coroutineScope.launch {
                    anchoredDraggableState.animateTo(BottomSheetDragValue.COLLAPSED)
                }
            }
        }
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val systemBarOffsetFromBottom = contentWindowInsets.getBottom(this)
        val systemBarOffsetFromLeft = contentWindowInsets.getLeft(this, layoutDirection)
        val systemBarOffsetFromRight = contentWindowInsets.getRight(this, layoutDirection)

        val bottomBarPlaceable = measurables
            .fastFirst { it.layoutId == BottomBarLayoutIdTag }
            .measure(looseConstraints)
        val bottomBarHeight = bottomBarPlaceable.height
        val bottomBarOffsetFromBottom = (bottomBarHeight * bottomBarOffsetProgress + scrollBehavior.state.heightOffset).roundToInt()

        val bottomSheetHeight = LIST_TRACK_CONTAINER_HEIGHT.roundToPx()
        val bottomSheetOffsetFromBottom = bottomBarOffsetFromBottom.coerceAtLeast(systemBarOffsetFromBottom) + bottomSheetHeight
        val bottomSheetTrack = layoutHeight - bottomSheetOffsetFromBottom
        anchoredDraggableState.updateAnchors(
            DraggableAnchors {
                BottomSheetDragValue.COLLAPSED at bottomSheetTrack.toFloat()
                BottomSheetDragValue.EXPANDED at 0f
            }
        )

        val bottomSheetPlaceable = measurables
            .fastFirst { it.layoutId == BottomSheetLayoutIdTag }
            .measure(
                looseConstraints
                    .offset(
                        horizontal = -(systemBarOffsetFromLeft + systemBarOffsetFromRight)
                    )
                    .copy(
                        maxHeight = if (progress == 0f) bottomSheetOffsetFromBottom else layoutHeight
                    )
            )
        val fabPlaceable = measurables
            .fastFirst { it.layoutId == FabLayoutIdTag }
            .measure(
                looseConstraints.offset(
                    horizontal = -(systemBarOffsetFromLeft + systemBarOffsetFromRight)
                )
            )
        val snackbarPlaceable = measurables
            .fastFirst { it.layoutId == SnackbarLayoutIdTag }
            .measure(
                looseConstraints.offset(
                    horizontal = -(systemBarOffsetFromLeft + systemBarOffsetFromRight)
                )
            )

        val contentHeight = layoutHeight - bottomSheetOffsetFromBottom
//        val contentHeight = layoutHeight - if (isCollapsingBottomBar) systemBarOffsetFromBottom else bottomBarOffsetFromBottom
        val contentPlaceable = measurables
            .fastFirst { it.layoutId == ContentLayoutIdTag }
            .measure(
                constraints.copy(
                    minHeight = contentHeight,
                    maxHeight = contentHeight
                )
            )

        layout(
            width = layoutWidth,
            height = layoutHeight
        ) {
            val fabPaddingOffset = FAB_PADDING.roundToPx()
            val fabHeight = if (fabPlaceable.height != 0) { fabPlaceable.height + fabPaddingOffset } else { 0 }
            val fabOffsetFromBottom = bottomSheetOffsetFromBottom + fabHeight
            val snackbarHeight = snackbarPlaceable.height
            val snackbarOffsetFromBottom = fabOffsetFromBottom + snackbarHeight
            contentPlaceable.placeRelative(
                x = 0,
                y = 0
            )
            fabPlaceable.placeRelative(
                x = if (layoutDirection == LayoutDirection.Ltr) {
                    layoutWidth - (fabPaddingOffset + fabPlaceable.width)
                } else { fabPaddingOffset },
                y = layoutHeight - fabOffsetFromBottom
            )
            bottomSheetPlaceable.placeRelative(
                x = 0,
                y = bottomSheetOffset
            )
            val transitionRatio = (bottomSheetOffset.toFloat() / bottomSheetTrack)
                .pow(2)
                .coerceIn(0f..1f)
            val bottomBarTransitionOffset = (bottomBarOffsetFromBottom * transitionRatio)
                .roundToInt()
            bottomBarPlaceable.placeRelative(
                x = 0,
                y = layoutHeight - bottomBarTransitionOffset
            )
            val snackbarTransitionOffset = (snackbarOffsetFromBottom * transitionRatio)
                .roundToInt()
                .coerceAtLeast(systemBarOffsetFromBottom + snackbarHeight)
            snackbarPlaceable.placeRelative(
                x = (layoutWidth - snackbarPlaceable.width) / 2 + systemBarOffsetFromLeft,
                y = layoutHeight - snackbarTransitionOffset
            )
        }
    }
}
