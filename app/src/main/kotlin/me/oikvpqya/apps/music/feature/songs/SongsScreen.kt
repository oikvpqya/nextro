package me.oikvpqya.apps.music.feature.songs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.oikvpqya.compose.fastscroller.rememberScrollbarAdapter
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.FastScrollerScrollbar
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.component.TrackContainer
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE

@Composable
internal fun SongsScreen(
    items: List<Library.Song>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    val scrollableState = rememberLazyListState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        MainContent(
            scrollableState = scrollableState,
            items = items,
            onItemClick = onItemClick,
        )
        FastScrollerScrollbar(
            modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollableState),
        )
    }
}

@Composable
private fun BoxScope.MainContent(
    scrollableState: LazyListState,
    items: List<Library.Song>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        state = scrollableState,
        modifier = Modifier
    ) {
        itemsIndexed(items = items) { index, song ->
            TrackContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(index)
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

//    val itemsAvailable = items.size.coerceAtLeast(1)
//    val scrollbarState = scrollableState.scrollbarState(
//        itemsAvailable = itemsAvailable,
//    )
//    scrollableState.DraggableScrollbar(
//        modifier = Modifier
//            .fillMaxHeight()
//            .padding(horizontal = 2.dp)
//            .align(Alignment.TopEnd),
//        state = scrollbarState,
//        orientation = Orientation.Vertical,
//        onThumbMoved = scrollableState.rememberDraggableScroller(
//            itemsAvailable = itemsAvailable,
//        ),
//    )
}
