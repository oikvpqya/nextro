package me.oikvpqya.apps.music.feature.player.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
fun TimestampContainer(
    duration: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium,
) {
    val timestamp = duration.milliseconds.asTimestamp()
    Box(
        modifier = modifier
    ) {
        Text(
            text = "0".repeat(timestamp.length),
            style = style.copy(color = Color.Transparent),
        )
        Text(
            text = timestamp,
            color = MaterialTheme.colorScheme.onSurface,
            style = style.copy(color = MaterialTheme.colorScheme.onSurface)
        )
    }
}

private fun Duration.asTimestamp(): String {
    val date = inWholeDays.floorDiv(1.days.inWholeDays)
    val hour = inWholeHours % 1.days.inWholeHours
    val minutes = inWholeMinutes % 1.hours.inWholeMinutes
    val seconds = inWholeSeconds % 1.minutes.inWholeSeconds
    return when {
        date == 0L && hour == 0L -> String.format("%02d:%02d", minutes, seconds)
        date == 0L -> String.format("%02d:%02d:%02d", hour, minutes, seconds)
        else -> String.format("%02d:%02d:%02d:%02d", date, hour, minutes, seconds)
    }
}
