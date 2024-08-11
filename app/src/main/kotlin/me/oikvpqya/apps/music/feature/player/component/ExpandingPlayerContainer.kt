package me.oikvpqya.apps.music.feature.player.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.media3.compose.LocalMediaInfoState
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.component.UI_PLAYER_SHARED_KEY_ARTWORK
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.component.fake.ImageMaxContainerSample
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_NORMAL_SIZE
import kotlin.math.max

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExpandingPlayerContainer(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
) {
    val mediaInfo by LocalMediaInfoState.current
    val progress = mediaInfo.progress
    val queueIndex = mediaInfo.queueIndex
    val queue = mediaInfo.queue
//    val queued = queueIndex >= 0 && queue.size > queueIndex
    val mediaHandler by LocalMediaHandlerState.current
    val savedSong = if (queueIndex >= 0 && queue.size > queueIndex) queue[queueIndex] else null
    val isFavorite = mediaHandler?.isFavoriteSharedFlow(mediaInfo.song ?: savedSong)
        ?.collectAsStateWithLifecycle(initialValue = null)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            Row {
                Layout(
                    contents = listOf<@Composable () -> Unit>(
                        {
                            with(sharedTransitionScope) {
                                ArtworkContainer(
                                    modifier = Modifier
                                        .sharedElement(
                                            state = rememberSharedContentState(
                                                key = UI_PLAYER_SHARED_KEY_ARTWORK
                                            ),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                        .clip(RoundedCornerShape(12.dp))
                                        .fillMaxSize(),
                                    artwork = mediaInfo.song?.getArtworkUri()
                                        ?: savedSong?.tag?.getArtworkUri(),
                                )
                            }
                        },
                        {
                            ToolbarContainer(
                                isFavorite = isFavorite?.value ?: false,
                                onFavoriteClick = {
                                    if (isFavorite?.value == false) {
                                        mediaHandler?.like(mediaInfo.song ?: savedSong)
                                    } else if (isFavorite?.value == true) {
                                        mediaHandler?.unlike(mediaInfo.song ?: savedSong)
                                    }
                                },
                                artwork = queue.getOrNull(queueIndex + 1)?.getArtworkUri(),
                            )
                        },
                    ),
                ) { (artworkMeasurables, toolbarMeasurables), constraints ->
                    val looseConstraints = constraints.copy(
                        minWidth = 0,
                        minHeight = 0,
                    )
                    val layoutWidth = constraints.maxWidth
//                    val layoutHeight = constraints.maxHeight
                    val toolbarPlaceable = toolbarMeasurables.fastMap { measurable ->
                        measurable.measure(
                            looseConstraints
                        )
                    }
                    val toolbarWidth = toolbarPlaceable.fastMaxBy { it.width }?.width ?: 0
                    val toolbarHeight = toolbarPlaceable.fastMaxBy { it.height }?.height ?: 0
                    val horizontalPadding = 16.dp.roundToPx()
                    val artworkMaxSize = (layoutWidth - horizontalPadding * 3 - toolbarWidth)
                        .coerceAtLeast(0)

                    val artworkPlaceable = artworkMeasurables.fastMap { measurable ->
                        measurable.measure(
                            looseConstraints
                                .copy(
                                    minWidth = artworkMaxSize,
                                    minHeight = artworkMaxSize,
                                    maxWidth = artworkMaxSize,
                                    maxHeight = artworkMaxSize,
                                )
                        )
                    }

                    layout(
                        width = layoutWidth,
                        height = max(
                            a = artworkMaxSize,
                            b = toolbarHeight,
                        ),
                    ) {
                        artworkPlaceable.fastForEach { placeable ->
                            placeable.placeRelative(
                                x = horizontalPadding,
                                y = 0,
                            )
                        }
                        toolbarPlaceable.fastForEach { placeable ->
                            placeable.placeRelative(
                                x = horizontalPadding * 2 + artworkMaxSize,
                                y = (artworkMaxSize - toolbarHeight)
                                    .coerceAtLeast(0),
                            )
                        }
                    }
                }
            }
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

@Composable
private fun ArtworkContainer(
    artwork: Any?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        if (artwork != null) {
            ImageMaxContainerSample(
                data = artwork,
            )
        } else {
            ImageMaxContainerSample()
        }
    }
}

@Composable
private fun ToolbarContainer(
    artwork: Any?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier.defaultMinSize(48.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                imageVector = AppIcons.Song,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (artwork != null) {
                ImageContainerSample(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    data = artwork,
                    size = LIST_TRACK_ALBUM_NORMAL_SIZE,
                )
            } else {
                ImageContainerSample(size = LIST_TRACK_ALBUM_NORMAL_SIZE)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        IconButton(
            onClick = onFavoriteClick,
        ) {
            Icon(
                imageVector = if (isFavorite) {
                    AppIcons.Favorite
                } else {
                    AppIcons.FavoriteBorder
                },
                contentDescription = null,
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {

            Icon(
                imageVector = AppIcons.MoreVert,
                contentDescription = null,
            )
        }
    }
}
