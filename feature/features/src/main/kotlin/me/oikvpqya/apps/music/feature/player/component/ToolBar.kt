package me.oikvpqya.apps.music.feature.player.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.media.LocalMediaHandlerState
import me.oikvpqya.apps.music.ui.media.LocalMediaInfoState

@Composable
fun ToolBar(
    modifier: Modifier = Modifier
) {
    val mediaHandler by LocalMediaHandlerState.current
    val mediaInfo by LocalMediaInfoState.current
    val queueIndex = mediaInfo.queueIndex
    val queue = mediaInfo.queue
    val savedSong = if (queueIndex >= 0 && queue.size > queueIndex) queue[queueIndex] else null
    val isFavorite = mediaHandler?.isFavoriteSharedFlow(mediaInfo.song ?: savedSong)?.collectAsStateWithLifecycle(initialValue = null)
    BottomAppBar(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isFavorite?.value == false) {
                        mediaHandler?.like(mediaInfo.song ?: savedSong)
                    } else if (isFavorite?.value == true)
                        mediaHandler?.unlike(mediaInfo.song ?: savedSong)
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = if (isFavorite?.value == true) AppIcons.Favorite else AppIcons.FavoriteBorder,
                    contentDescription = null
                )
            }
        },
        actions = {}
    )
}

@Composable
private fun RowScope.Actions() {
    IconButton(onClick = { /* do something */ }) {
        Icon(
            imageVector = AppIcons.Lyrics,
            contentDescription = null
        )
    }
    IconButton(onClick = { /* do something */ }) {
        Icon(
            imageVector = AppIcons.Favorite,
            contentDescription = null
        )
    }
    IconButton(onClick = { /* do something */ }) {
        Icon(
            imageVector = AppIcons.Playlists,
            contentDescription = null
        )
    }
    IconButton(onClick = { /* do something */ }) {
        Icon(
            imageVector = AppIcons.More,
            contentDescription = null
        )
    }
}
