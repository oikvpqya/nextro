package me.oikvpqya.apps.music.feature.player.component

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerLayoutInfo
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.oikvpqya.apps.music.feature.player.ARTWORK_PADDING
import me.oikvpqya.apps.music.feature.player.CAROUSEL_DEFAULT_MIN_WIDTH
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.media3.compose.LocalMediaInfoState
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerLayoutSample
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun ArtworkCarouselContainer(
    itemSize: Dp,
    pagerState: PagerState,
    flingBehavior: TargetedFlingBehavior,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val mediaInfo by LocalMediaInfoState.current
    val mediaHandler by LocalMediaHandlerState.current
    LaunchedEffect(mediaInfo) {
        Log.d("PlayerScreen", "mediaInfo: index: ${mediaInfo.queueIndex} progress: ${mediaInfo.progress}")
    }
    val queue = mediaInfo.queue.map { it.getArtworkUri() }
    val queueIndex by remember { derivedStateOf { mediaInfo.queueIndex } }
    val scope = rememberCoroutineScope()
    val queued = queueIndex >= 0 && queue.size > queueIndex
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val targetPage by remember {
        derivedStateOf { pagerState.targetPage }
    }

    val pageSize = itemSize - ARTWORK_PADDING * 2
    val pageSizePx = with(LocalDensity.current) { pageSize.toPx().roundToInt() }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        key = { index ->
            "player-screen-${index}"
        },
        pageSize = PageSize.Fixed(pageSize),
        contentPadding = PaddingValues(
//            start = ARTWORK_PADDING,
//            end = ARTWORK_PADDING,
            top = ARTWORK_PADDING, bottom = ARTWORK_PADDING
        ),
        pageSpacing = ARTWORK_PADDING,
        flingBehavior = flingBehavior,
//        userScrollEnabled = false
    ) { index ->
        Box(
            modifier = Modifier
                .size(pageSize)
        ) {
            val overflowedItemOffset by remember {
                derivedStateOf {
                    pagerState.layoutInfo.overflowedItemOffset(index, pageSizePx)
                }
            }
            val align = when {
                overflowedItemOffset < 0 -> Alignment.TopEnd
                else -> Alignment.TopStart
            }
            val dp = with(LocalDensity.current) {
                overflowedItemOffset.absoluteValue.toDp()
            }
            val alpha by animateFloatAsState(targetValue = if (index == targetPage) 1f else 0.32f, label = "")
            Box(
                modifier = Modifier
                    .align(align)
                    .height(pageSize)
                    .width((pageSize - dp).coerceAtLeast(CAROUSEL_DEFAULT_MIN_WIDTH))
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.scrim)
                    .alpha(alpha)
            ) {
                ImageContainerLayoutSample(
                    data = queue[index],
                    size = pageSize
                )
            }
        }
    }

    LaunchedEffect(queue, queueIndex) {
        if (queued) {
            scope.launch {
                while (isDragged) {
                    delay(100)
                }
                pagerState.animateScrollToPage(queueIndex, 0f)
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.targetPage
        }.collectLatest { page ->
            Log.d("PlayerScreen", "isDragged: $isDragged, page: $page, queueIndex: $queueIndex")
            if (queued && page != queueIndex) {
                while (isDragged) {
                    delay(100)
                }
                mediaHandler?.playAt(page, 0L)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun PagerLayoutInfo.overflowedItemOffset(index: Int, size: Int) : Int =
    visiblePagesInfo
        .firstOrNull { it.index == index }
        ?.let {
            val track = viewportEndOffset + viewportStartOffset - size
            when {
                it.offset < 0 -> it.offset
                it.offset > track -> it.offset - track
                else -> 0
            }
        }
        ?: 0
