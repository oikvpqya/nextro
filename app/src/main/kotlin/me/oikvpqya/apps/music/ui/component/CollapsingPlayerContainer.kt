package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.media3.compose.LocalMediaInfoState
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE

@Composable
fun CollapsingPlayerContainer(
    title: String,
    summary: String,
    isPlaying: Boolean,
    playingProgress: Float,
    onPlayOrPauseClick: () -> Unit,
    iconContainer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    TrackContainer(
        modifier = modifier,
        titleContainer = { TitleContainer(title) },
        summaryContainer = { SummaryContainer(summary) },
        iconContainer = iconContainer,
        widgetContainer = {
            Box(
                modifier = Modifier
                    .size(LIST_TRACK_ALBUM_SIZE)
                    .clip(RoundedCornerShape(percent = 44))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable(
//                        indication = rememberRipple(
//                            bounded = false,
//                            color = MaterialTheme.colorScheme.onSurface,
//                            radius = 22.dp
//                        ),
//                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onPlayOrPauseClick
                    )
            ) {
                CircularProgressIndicator(
                    progress = { playingProgress },
                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .clip(RoundedCornerShape(percent = 44))
//                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxSize(),
//                    color = MaterialTheme.colorScheme.secondaryContainer,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round,
                )
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center),
                    imageVector = if (isPlaying) AppIcons.Pause else AppIcons.Play,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        },
        enabledMarqueeText = true
    )
}

@Composable
fun CollapsingPlayerContainer(
    modifier: Modifier = Modifier,
) {
    val mediaInfo by LocalMediaInfoState.current
    val mediaHandler by LocalMediaHandlerState.current
    val queue = mediaInfo.queue
    val queueIndex = mediaInfo.queueIndex
    val savedSong = if (queueIndex >= 0 && queue.size > queueIndex) queue[queueIndex] else null
    CollapsingPlayerContainer(
        modifier = modifier,
        title = mediaInfo.song?.name ?: savedSong?.name ?: "Not Playing",
        summary = mediaInfo.song?.summary ?: savedSong?.summary ?: "${mediaInfo.state}",
        isPlaying = mediaInfo.isPlaying,
        playingProgress = mediaInfo.progress,
        onPlayOrPauseClick = {
            if (mediaInfo.song != null) {
                mediaHandler?.playOrPause()
            } else {
                mediaHandler?.playSongs(queue, queueIndex, 0L)
            }
        },
        iconContainer = {
            val data = mediaInfo.song?.getArtworkUri() ?: savedSong?.tag?.getArtworkUri()
            if (data != null) {
                ImageContainerSample(
                    data = data,
                    size = LIST_TRACK_ALBUM_SIZE
                )
            }
            else ImageContainerSample(size = LIST_TRACK_ALBUM_SIZE)
        }
    )
}
