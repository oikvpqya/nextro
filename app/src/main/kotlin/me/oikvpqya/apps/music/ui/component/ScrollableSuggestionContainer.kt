package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE

@Composable
fun ScrollableSuggestionContainer(
    songs: List<Library.Song>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(songs) { item ->
            ChannelContainer(
                modifier = Modifier
                    .width(LIST_TRACK_ALBUM_SIZE * 3),
                titleContainer = { TitleContainer(item.name) },
                summaryContainer = { SummaryContainer(summary = item.summary) },
            ) {}
        }
    }
}
