package me.oikvpqya.apps.music.feature.library.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.ui.component.ChannelContainer
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.component.fake.ImageMaxContainerSample

@Composable
fun LibrariesGridScreen(
    libraries: ImmutableList<Libraries>,
    modifier: Modifier = Modifier,
    onItemClick: (Libraries) -> Unit
) {
    val scrollableState = rememberLazyGridState()

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MainContent(
                scrollableState = scrollableState,
                libraries = libraries,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun BoxScope.MainContent(
    scrollableState: LazyGridState,
    libraries: ImmutableList<Libraries>,
    onItemClick: (Libraries) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        state = scrollableState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(libraries) { item ->

            ChannelContainer(
                modifier = Modifier.fillMaxWidth(),
                titleContainer = { TitleContainer(title = item.name) },
                summaryContainer = { SummaryContainer(summary = item.summary) },
                iconContainer = {
                    ImageMaxContainerSample(data = item.getArtworkUri(), isMaxWidth = true)
                }
            ) {
                onItemClick(item)
            }
        }
    }

//    val itemsAvailable = libraries.size.coerceAtLeast(1)
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
//            itemsAvailable = itemsAvailable.coerceAtLeast(1),
//        ),
//    )
}

//@Composable
//internal fun AlbumsScreen(
//    contentPadding: PaddingValues,
//    modifier: Modifier = Modifier,
//) {
//    val title = "album"
//    val summary = "summary"
//    LazyVerticalGrid(
//        modifier = modifier
//            .padding(horizontal = 16.dp),
//        columns = GridCells.Fixed(2),
//        contentPadding = contentPadding,
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//    ) {
//        items(20) {
//            ChannelContainer(
//                modifier = Modifier.fillMaxWidth(),
//                titleContainer = { TitleContainer(title) },
//                summaryContainer = { SummaryContainer(summary) },
//            ) {}
//        }
//    }
//}

//@Composable
//private fun BoxScope.MainContent(
//    scrollableState: LazyStaggeredGridState,
//    items: List<Artist>,
//    itemsAvailable: Int,
//    nestedScrollConnection: NestedScrollConnection,
//    modifier: Modifier = Modifier,
//    onItemClick: () -> Unit
//) {
//    LazyVerticalStaggeredGrid(
//        modifier = modifier
//            .padding(horizontal = 16.dp)
//            .nestedScroll(nestedScrollConnection),
//        columns = StaggeredGridCells.Fixed(2),
//        state = scrollableState,
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalItemSpacing = 16.dp,
//    ) {
//        items(items) { artist ->
//            ChannelContainer(
//                modifier = Modifier.fillMaxWidth(),
//                titleContainer = { TitleContainer(artist.name) },
//                iconContainer = { ImageMaxContainerSample(data = artist.songs[0].artworkUri) }
//            ) { onItemClick() }
//        }
//    }
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
//}
