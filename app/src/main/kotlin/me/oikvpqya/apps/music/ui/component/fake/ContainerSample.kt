package me.oikvpqya.apps.music.ui.component.fake

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun ImageContainerSample(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun ImageContainerSample(
    data: Any,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    AsyncImage(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp)),
        model = data,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ImageContainerLayoutSample(
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    radius: Dp = 12.dp,
    size: Dp = 32.dp,
) {
    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val sizePx = size.toPx().roundToInt()
                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = sizePx,
                        maxWidth = sizePx,
                        minHeight = sizePx,
                        maxHeight = sizePx
                    )
                )
                layout(sizePx, sizePx) {
                    placeable.placeRelative(0, 0)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clip(RoundedCornerShape(radius))
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
fun ImageContainerLayoutSample(
    data: Any,
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    radius: Dp = 12.dp,
    size: Dp = 32.dp,
) {
    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val sizePx = size.toPx().roundToInt()
                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = sizePx,
                        maxWidth = sizePx,
                        minHeight = sizePx,
                        maxHeight = sizePx
                    )
                )
                layout(sizePx, sizePx) {
                    placeable.placeRelative(0, 0)
                }
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clip(RoundedCornerShape(radius)),
            model = data,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ImageMaxContainerSample(
    modifier: Modifier = Modifier,
    isMaxWidth: Boolean? = null,
    padding: Dp = 0.dp,
    radius: Dp = 12.dp,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .background(MaterialTheme.colorScheme.primary)
            .layout { measurable, constraints ->
                val size = with(constraints) {
                    when (isMaxWidth) {
                        true -> maxWidth
                        false -> maxHeight
                        null -> maxOf(maxWidth, maxWidth)
                    }
                }
                layout(size, size) {
                    measurable
                        .measure(
                            constraints.copy(
                                minWidth = size,
                                maxWidth = size,
                                minHeight = size,
                                maxHeight = size
                            )
                        )
                        .placeRelative(0, 0)
                }
            }
            .fillMaxSize()
            .padding(padding),
    )
}

@Composable
fun ImageMaxContainerSample(
    data: Any,
    modifier: Modifier = Modifier,
    isMaxWidth: Boolean? = null,
    padding: Dp = 0.dp,
    radius: Dp = 12.dp,
) {
    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val size = with(constraints) {
                    when (isMaxWidth) {
                        true -> maxWidth
                        false -> maxHeight
                        null -> maxOf(maxWidth, maxWidth)
                    }
                }
                layout(size, size) {
                    measurable
                        .measure(
                            constraints.copy(
                                minWidth = size,
                                maxWidth = size,
                                minHeight = size,
                                maxHeight = size
                            )
                        )
                        .placeRelative(0, 0)
                }
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clip(RoundedCornerShape(radius)),
            model = data,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
