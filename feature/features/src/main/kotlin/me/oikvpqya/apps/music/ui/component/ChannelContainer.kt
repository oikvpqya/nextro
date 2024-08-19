package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.ui.component.fake.ImageMaxContainerSample

@Composable
fun ChannelContainer(
    titleContainer: @Composable (() -> Unit),
    summaryContainer: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconContainer: @Composable (() -> Unit) = { ImageMaxContainerSample() },
//    widgetContainer: @Composable (() -> Unit) = { ImageContainerSample() },
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled, onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(bottom = 16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        iconContainer()
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            titleContainer()
            summaryContainer()
        }
    }
}
@Composable
fun ChannelContainer(
    titleContainer: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconContainer: @Composable (() -> Unit) = { ImageMaxContainerSample() },
//    widgetContainer: @Composable (() -> Unit) = { ImageContainerSample() },
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
            .then(
                if (onClick != null) {
                    Modifier.clickable(enabled, onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(bottom = 16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        iconContainer()
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            titleContainer()
        }
    }
}

// With widgetContainer()
//
//Row(
//    modifier = Modifier.padding(horizontal = 16.dp),
//    horizontalArrangement = Arrangement.spacedBy(16.dp),
//    verticalAlignment = Alignment.CenterVertically
//) {
//    Column(
//        modifier = Modifier.weight(1f)
//    ) {
//        titleContainer()
//        summaryContainer()
//    }
//    widgetContainer()
//}
