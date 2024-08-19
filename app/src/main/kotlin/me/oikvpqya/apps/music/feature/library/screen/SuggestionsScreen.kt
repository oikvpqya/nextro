package me.oikvpqya.apps.music.feature.library.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.RandomTiledLibraryContainer
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.component.TrackContainer
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.media.LocalMediaHandlerState
import me.oikvpqya.apps.music.ui.media.getArtworkUri
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE

@Composable
fun SuggestionsScreen(
    suggestionSongs: ImmutableList<Library.Song>,
    isSheetExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollableState = rememberLazyListState()
    val mediaHandler by LocalMediaHandlerState.current
    val playSongAction: (List<Library. Song>, Int) -> Unit = { songs, index ->
        mediaHandler?.playSongs(songs, index)
    }
    if (suggestionSongs.size == 8) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = scrollableState,
            userScrollEnabled = isSheetExpanded,
        ) {
            item {
                RandomTiledLibraryContainer(
                    items = suggestionSongs,
                    onItemClick = { items, index ->
                        playSongAction(items, index)
                    }
                )
            }
            itemsIndexed(suggestionSongs) { index, item ->
                TrackContainer(
                    modifier = Modifier
                        .fillMaxWidth(),
                    titleContainer = { TitleContainer(item.name) },
                    summaryContainer = { SummaryContainer(item.summary) },
                    iconContainer = {
                        ImageContainerSample(
                            data = item.getArtworkUri(),
                            size = LIST_TRACK_ALBUM_SIZE
                        )
                    },
                    widgetContainer = {
//                    Icon(imageVector = AppIcons.More, contentDescription = null)
                    },
                    onClick = {
                        playSongAction(suggestionSongs, index)
                    },
                )
            }
        }
    }
}
