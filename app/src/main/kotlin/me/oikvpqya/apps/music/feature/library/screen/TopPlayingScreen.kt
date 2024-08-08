package me.oikvpqya.apps.music.feature.library.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import me.oikvpqya.apps.music.media3.compose.LocalMediaHandlerState
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.ColumnLibrariesContainer
import me.oikvpqya.apps.music.ui.component.ColumnLibraryContainer
import me.oikvpqya.apps.music.ui.component.HeaderContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer

@Composable
fun TopPlayingScreen(
    topAlbums: ImmutableList<Libraries.Album>,
    topArtists: ImmutableList<Libraries.Default>,
    topSongs: ImmutableList<Library.Song>,
    isSheetExpanded: Boolean,
    onLibrariesClick: (Libraries) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaHandler by LocalMediaHandlerState.current
    val scrollableState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = scrollableState,
        userScrollEnabled = isSheetExpanded,
    ) {
        homeLibraryContainer(
            title = "Top Songs",
            items = topSongs,
            onHeaderClick = {},
            onItemClick = { index ->
                mediaHandler?.playSongs(topSongs, index)
            },
            onMoreClick = {}
        )
        homeLibrariesContainer(
            title = "Top Artist",
            items = topArtists,
            onHeaderClick = {},
            onItemClick = onLibrariesClick,
            onMoreClick = {}
        )
        homeLibrariesContainer(
            title = "Top Album",
            items = topAlbums,
            onHeaderClick = {},
            onItemClick = onLibrariesClick,
            onMoreClick = {}
        )
    }
}

private fun LazyListScope.homeLibraryContainer(
    title: String,
    items: ImmutableList<Library.Song>,
    onHeaderClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onMoreClick: () -> Unit
) {
    item(
        key = "header_library_${title}"
    ) {
        HeaderContainer(
            titleContainer = { TitleContainer(title) },
            widgetContainer = {
//                    ImageContainerSample()
            },
            onClick = onHeaderClick
        )
    }
    item(
        key = "container_library_${title}"
    ) {
        ColumnLibraryContainer(
            items = items,
            onItemClick = onItemClick
        )
    }
}

private fun LazyListScope.homeLibrariesContainer(
    title: String,
    items: ImmutableList<Libraries>,
    onHeaderClick: () -> Unit,
    onItemClick: (Libraries) -> Unit,
    onMoreClick: () -> Unit
) {
    item(
        key = "header_libraries_${title}"
    ) {
        HeaderContainer(
            titleContainer = { TitleContainer(title) },
            widgetContainer = {
//                    ImageContainerSample()
            },
            onClick = onHeaderClick
        )
    }
    item(
        key = "container_libraries_${title}"
    ) {
        ColumnLibrariesContainer(
            libraries = items,
            onItemClick = onItemClick
        )
    }
}

