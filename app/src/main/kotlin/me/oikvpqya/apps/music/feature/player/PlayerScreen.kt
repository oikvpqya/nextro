package me.oikvpqya.apps.music.feature.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.feature.player.component.ArtworkCarouselContainer
import me.oikvpqya.apps.music.feature.player.component.ControlBar
import me.oikvpqya.apps.music.feature.player.component.ProgressSlider
import me.oikvpqya.apps.music.feature.player.component.ToolBar
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.media3.compose.LocalMediaInfoState
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer

val CAROUSEL_MIN_WIDTH = 40.dp..56.dp
val CAROUSEL_DEFAULT_MIN_WIDTH = CAROUSEL_MIN_WIDTH.start
val ARTWORK_PADDING = 16.dp
const val DISPLAY_NEXT_MAX_COUNT = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "Now Playing") }
        )
        MainContent(
            modifier = Modifier.weight(1f),
        )
        ToolBar()
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {
    val mediaInfo by LocalMediaInfoState.current
    val progress = mediaInfo.progress
    val queueIndex = mediaInfo.queueIndex
    val queue = mediaInfo.queue
    val queued = queueIndex >= 0 && queue.size > queueIndex
    val pagerState = rememberPagerState(
        initialPage = if (queued) queueIndex else 0,
        initialPageOffsetFraction = 0f
    ) { queue.size }
    val flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
//    val interactionSource = remember { MutableInteractionSource() }
    val mediaHandler by LocalMediaHandlerState.current
    val savedSong = if (queueIndex >= 0 && queue.size > queueIndex) queue[queueIndex] else null
    BoxWithConstraints(
        modifier = modifier
//            .scrollable(
//                state = pagerState,
//                orientation = Orientation.Horizontal,
//                reverseDirection = true,
//                flingBehavior = flingBehavior,
//                interactionSource = interactionSource,
//                overscrollEffect = ScrollableDefaults.overscrollEffect()
//            )
    ) {
        val itemSize = maxWidth - (ARTWORK_PADDING + CAROUSEL_DEFAULT_MIN_WIDTH) * DISPLAY_NEXT_MAX_COUNT * 2
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ArtworkCarouselContainer(
                itemSize = itemSize,
                pagerState = pagerState,
                flingBehavior = flingBehavior
            )
            TitleContainer(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                title = mediaInfo.song?.name ?: savedSong?.name ?: "Not Playing",
                style = MaterialTheme.typography.titleLarge
            )
            SummaryContainer(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                summary = mediaInfo.song?.summary ?: savedSong?.summary ?: "${mediaInfo.state}",
                style = MaterialTheme.typography.titleMedium
            )
            ProgressSlider(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                duration = mediaInfo.song?.tag?.duration ?: 0L,
                position = mediaInfo.position,
                progress = progress,
                onValueChangeFinished = {
                    mediaHandler?.playAt(position = it)
                }
            )
            ControlBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
