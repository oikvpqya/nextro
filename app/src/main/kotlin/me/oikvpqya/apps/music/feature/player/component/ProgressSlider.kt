package me.oikvpqya.apps.music.feature.player.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import ir.mahozad.multiplatform.wavyslider.material3.Track as WavyTrack
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class)
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
        Slider(
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
            },
            track = { sliderState ->
                SliderDefaults.WavyTrack(
                    sliderState = sliderState,
                    incremental = true,
                )
            },
        )
        TimestampContainer(duration = duration)
    }

    LaunchedEffect(progress) {
        if (!onSliding) sliderValue = progress
    }
}
