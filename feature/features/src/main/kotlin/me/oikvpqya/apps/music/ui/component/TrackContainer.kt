package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_SIZE
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_CONTAINER_HEIGHT

@Composable
fun TrackContainer(
    titleContainer: @Composable (() -> Unit),
    summaryContainer: @Composable (() -> Unit),
    widgetContainer: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    enabledMarqueeText: Boolean = false,
    iconContainer: @Composable (() -> Unit) = { ImageContainerSample(size = LIST_TRACK_ALBUM_SIZE) },
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .height(LIST_TRACK_CONTAINER_HEIGHT)
            .padding(horizontal = 16.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled, onClick = onClick)
                } else {
                    Modifier
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconContainer()
        if (enabledMarqueeText) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                titleContainer()
                summaryContainer()
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                titleContainer()
                summaryContainer()
            }
        }
        widgetContainer()
    }
}

@Composable
fun TitleContainer(
    title: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Text(
        text = title,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style = style,
    )
}

@Composable
fun SummaryContainer(
    summary: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = summary,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = style,
    )
}
