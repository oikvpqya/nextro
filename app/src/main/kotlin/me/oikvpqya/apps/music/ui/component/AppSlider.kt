package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val TickSize = 2.dp
private val TrackHeight = 48.dp
private val TrackStrokeWidth = 4.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit),
    modifier: Modifier = Modifier,
    imageVector: ImageVector = AppIcons.Song,
    enabled: Boolean = true,
    steps: Int = 0,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        thumb = { _ ->
            Icon(
                modifier = Modifier,
                imageVector = imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        track = { _ ->
            Track(
                steps = steps,
                value = value,
                valueRange = valueRange
            )
        },
        enabled = enabled,
        steps = steps,
        valueRange = valueRange
    )
}

@Composable
private fun Track(
    steps: Int,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    val inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
    val activeTrackColor = MaterialTheme.colorScheme.secondaryContainer
    val inactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant
    val activeTickColor = MaterialTheme.colorScheme.onSecondaryContainer

    Canvas(
        modifier
            .fillMaxWidth()
            .height(TrackHeight)
    ) {
        drawTrack(
            tickFractions = stepsToTickFractions(steps),
            activeRangeStart = 0f,
            activeRangeEnd = calcFraction(
                valueRange.start,
                valueRange.endInclusive,
                value.coerceIn(valueRange.start, valueRange.endInclusive)
            ),
            inactiveTrackColor = inactiveTrackColor,
            activeTrackColor = activeTrackColor,
            inactiveTickColor = inactiveTickColor,
            activeTickColor = activeTickColor
        )
    }
}

private fun DrawScope.drawTrack(
    tickFractions: FloatArray,
    activeRangeStart: Float,
    activeRangeEnd: Float,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    inactiveTickColor: Color,
    activeTickColor: Color
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val tickSize = TickSize.toPx()
    val inactiveTrackStrokeWidth = TrackStrokeWidth.toPx()
    val activeTrackStrokeWidth = TrackHeight.toPx()
    drawLine(
        inactiveTrackColor,
        sliderStart,
        sliderEnd,
        inactiveTrackStrokeWidth,
        StrokeCap.Round
    )
    val sliderValueEnd = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeEnd,
        center.y
    )

    val sliderValueStart = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeStart,
        center.y
    )

    drawLine(
        activeTrackColor,
        sliderValueStart,
        sliderValueEnd,
        activeTrackStrokeWidth,
        StrokeCap.Round
    )

    for (tick in tickFractions) {
        val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
        drawCircle(
            color = if (outsideFraction) inactiveTickColor else activeTickColor,
            center = Offset(lerp(sliderStart, sliderEnd, tick).x, center.y),
            radius = tickSize / 2f
        )
    }
}

private fun stepsToTickFractions(steps: Int): FloatArray {
    return if (steps == 0) floatArrayOf() else FloatArray(steps + 2) { it.toFloat() / (steps + 1) }
}

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)
