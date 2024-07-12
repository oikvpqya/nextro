package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE

@Composable
fun ColumnLibrariesContainer(
    libraries: List<Libraries>,
    onItemClick: (Libraries) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        libraries.forEach { item ->
            TrackContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    },
                titleContainer = { TitleContainer(item.name) },
                summaryContainer = { SummaryContainer(item.summary) },
                iconContainer = { ImageContainerSample(data = item.getArtworkUri(), size = LIST_TRACK_ALBUM_SIZE) },
                widgetContainer = {
//                    Icon(imageVector = AppIcons.More, contentDescription = null)
                }
            )
        }
    }
}
@Composable
fun ColumnLibraryContainer(
    items: List<Library.Song>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        items.forEachIndexed { index, song ->
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
//                    Icon(imageVector = AppIcons.More, contentDescription = null)
                }
            )
        }
    }
}
