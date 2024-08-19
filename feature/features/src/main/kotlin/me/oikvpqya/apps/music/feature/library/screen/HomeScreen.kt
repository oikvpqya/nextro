package me.oikvpqya.apps.music.feature.library.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.ColumnLibrariesContainer
import me.oikvpqya.apps.music.ui.component.ColumnLibraryContainer
import me.oikvpqya.apps.music.ui.component.HeaderContainer
import me.oikvpqya.apps.music.ui.component.RandomTiledLibraryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.media.LocalMediaHandlerState

@Deprecated(
    message = "unused"
)
@Composable
fun HomeScreen(
    favoriteSongs: ImmutableList<Library.Song>,
    historySongs: ImmutableList<Library.Song>,
    suggestionSongs: ImmutableList<Library.Song>,
    topAlbums: ImmutableList<Libraries.Album>,
    topArtists: ImmutableList<Libraries.Default>,
    topSongs: ImmutableList<Library.Song>,
    modifier: Modifier = Modifier,
    onLibrariesClick: (Libraries) -> Unit
) {
    val scrollableState = rememberLazyListState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        MainContent(
            favoriteSongs = favoriteSongs,
            historySongs = historySongs,
            suggestionSongs = suggestionSongs,
            topSongs = topSongs,
            topArtists = topArtists,
            topAlbums = topAlbums,
            scrollableState = scrollableState,
            onLibrariesClick = onLibrariesClick
        )
    }
}

@Deprecated(
    message = "unused"
)
@Composable
private fun BoxScope.MainContent(
    favoriteSongs: ImmutableList<Library.Song>,
    historySongs: ImmutableList<Library.Song>,
    suggestionSongs: ImmutableList<Library.Song>,
    topAlbums: ImmutableList<Libraries.Album>,
    topArtists: ImmutableList<Libraries.Default>,
    topSongs: ImmutableList<Library.Song>,
    scrollableState: LazyListState,
    onLibrariesClick: (Libraries) -> Unit
) {
    val mediaHandler by LocalMediaHandlerState.current
    LazyColumn(
        modifier = Modifier,
        state = scrollableState
    ) {
        homeLibraryContainer(
            title = "History",
            items = historySongs,
            onHeaderClick = {},
            onItemClick = { index ->
                mediaHandler?.playSongs(historySongs, index)
            },
            onMoreClick = {}
        )
        item {
            HeaderContainer(
                titleContainer = { TitleContainer("Mix") },
                widgetContainer = {
//                    ImageContainerSample()
                }
            ) {}
        }
        item {
            if (suggestionSongs.size == 8) {
                RandomTiledLibraryContainer(
                    items = suggestionSongs,
                    onItemClick = { items, index  ->
                        mediaHandler?.playSongs(items, index)
                    }
                )
            }
        }
        homeLibraryContainer(
            title = "Favorite",
            items = favoriteSongs,
            onHeaderClick = {},
            onItemClick = { index ->
                mediaHandler?.playSongs(favoriteSongs, index)
            },
            onMoreClick = {}
        )
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
