package me.oikvpqya.apps.music.feature.library.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.oikvpqya.apps.music.feature.library.component.LibrariesDetailScreenHeader
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.component.TrackContainer
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE

@Composable
fun LibrariesDetailScreen(
    name: String,
    summary: String,
    albumId: Long,
    songs: List<Library.Song>,
    firstItems: List<Libraries>,
    secondItems: List<Libraries>,
    isSheetExpanded: Boolean,
    onLibrariesClick: (Libraries) -> Unit,
    onSongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberLazyListState()

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MainContent(
                name = name,
                summary = summary,
                albumId = albumId,
                songs = songs,
                firstItems = firstItems,
                secondItems = secondItems,
                scrollableState = scrollableState,
                isSheetExpanded = isSheetExpanded,
                onLibrariesClick = onLibrariesClick,
                onSongClick = onSongClick
            )
        }
    }
}

@Composable
private fun BoxScope.MainContent(
    name: String,
    summary: String,
    albumId: Long,
    songs: List<Library.Song>,
    firstItems: List<Libraries>,
    secondItems: List<Libraries>,
    scrollableState: LazyListState,
    isSheetExpanded: Boolean,
    onLibrariesClick: (Libraries) -> Unit,
    onSongClick: (Int) -> Unit
) {
    songs.ifEmpty { return }
    LazyColumn(
        state = scrollableState,
        modifier = Modifier,
        userScrollEnabled = isSheetExpanded,
    ) {
        item { LibrariesDetailScreenHeader(name, summary, albumId) }

        itemsIndexed(items = songs) { index, song ->
            TrackContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSongClick(index)
                    },
                titleContainer = { TitleContainer(song.name) },
                summaryContainer = { SummaryContainer(song.summary) },
                iconContainer = { ImageContainerSample(data = song.getArtworkUri(), size = LIST_TRACK_ALBUM_SIZE) },
                widgetContainer = {
//                        Icon(imageVector = AppIcons.More, contentDescription = null)
                }
            )
        }
    }
//    val itemsAvailable = firstItems.isNotEmpty().toInt() + secondItems.isNotEmpty().toInt() + songs.size + 1
//    scrollableState.DraggableScrollbar(
//        modifier = Modifier
//            .fillMaxHeight()
//            .padding(horizontal = 2.dp)
//            .align(Alignment.TopEnd),
//        state = scrollableState.scrollbarState(
//            itemsAvailable = itemsAvailable,
//        ),
//        orientation = Orientation.Vertical,
//        onThumbMoved = scrollableState.rememberDraggableScroller(
//            itemsAvailable = itemsAvailable,
//        ),
//    )
}

private fun Boolean.toInt(): Int {
    return when (this) {
        true -> 1
        false -> 0
    }
}
