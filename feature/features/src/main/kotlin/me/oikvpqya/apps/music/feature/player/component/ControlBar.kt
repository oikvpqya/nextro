package me.oikvpqya.apps.music.feature.player.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.model.PlaybackRepeatMode
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.media.LocalMediaHandlerState
import me.oikvpqya.apps.music.ui.media.LocalMediaInfoState

@Composable
fun ControlBar(
    modifier: Modifier = Modifier,
) {
    val mediaInfo by LocalMediaInfoState.current
    val queueIndex = mediaInfo.queueIndex
    val queue = mediaInfo.queue
    val mediaHandler by LocalMediaHandlerState.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
//            .clip(RoundedCornerShape(24.dp))
//            .background(MaterialTheme.colorScheme.surfaceVariant)
        ,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically

    ) {
        IconButton(
            onClick = {
                when (mediaInfo.repeatMode) {
                    PlaybackRepeatMode.OFF -> mediaHandler?.enableRepeatAllMode()
                    PlaybackRepeatMode.ALL -> mediaHandler?.enableRepeatOneMode()
                    PlaybackRepeatMode.ONE -> mediaHandler?.disableRepeatMode()
                    null -> Unit
                }
            }
        ) {
            Icon(
                modifier = Modifier
                    .alpha(
                        when (mediaInfo.repeatMode) {
                            PlaybackRepeatMode.OFF, null -> 0.38f
                            else -> 1f
                        }
                    )
                ,
                imageVector = when (mediaInfo.repeatMode) {
                    PlaybackRepeatMode.OFF, null -> AppIcons.Repeat
                    PlaybackRepeatMode.ONE -> AppIcons.RepeatOne
                    PlaybackRepeatMode.ALL -> AppIcons.Repeat
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { mediaHandler?.seekToPrevious() }) {
            Icon(
                imageVector = AppIcons.Previous,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        FloatingActionButton(
            onClick = {
                if (mediaInfo.song != null) {
                    mediaHandler?.playOrPause()
                } else {
                    mediaHandler?.playSongs(queue, queueIndex, 0L)
                }
            },
            contentColor = MaterialTheme.colorScheme.secondaryContainer,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(
                imageVector = if (mediaInfo.isPlaying) { AppIcons.Pause } else { AppIcons.Play },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { mediaHandler?.seekToNext() }) {
            Icon(
                imageVector = AppIcons.Next,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(
            onClick = {
                when (mediaInfo.shuffleMode) {
                    true -> mediaHandler?.disableShuffleMode()
                    false -> mediaHandler?.enableShuffleMode()
                    null -> Unit
                }
            }
        ) {
            Icon(
                modifier = Modifier
                    .alpha(
                        if (mediaInfo.shuffleMode == true) 1f else 0.38f
                    )
                ,
                imageVector = AppIcons.Shuffle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
