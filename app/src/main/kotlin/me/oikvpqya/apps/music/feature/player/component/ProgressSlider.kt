package me.oikvpqya.apps.music.feature.player.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.ui.component.AppSlider
import kotlin.math.roundToLong

@Composable
fun ProgressSlider(
    duration: Long,
    position: Long,
    modifier: Modifier = Modifier,
    progress: Float = position.toFloat() / duration,
    onValueChangeFinished: (Long) -> Unit,
) {
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var onSliding by remember { mutableStateOf(false) }
    val sliderDuration = (sliderValue * duration).roundToLong()
    val animateProgress by animateFloatAsState(
        targetValue = if (onSliding) { sliderValue } else progress,
        label = "",
        animationSpec = tween(durationMillis = if (onSliding) 0 else 1000)
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimestampContainer(
            duration = if (onSliding) sliderDuration else position
        )
        AppSlider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp),
            value = animateProgress,
            onValueChange = { value ->
                onSliding = true
                sliderValue = value
            },
            onValueChangeFinished = {
                onSliding = false
                onValueChangeFinished(sliderDuration)
            }
        )
        TimestampContainer(duration = duration)
    }

    LaunchedEffect(progress) {
        if (!onSliding) sliderValue = progress
    }
}
