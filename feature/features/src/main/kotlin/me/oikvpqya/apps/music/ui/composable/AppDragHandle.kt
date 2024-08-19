package me.oikvpqya.apps.music.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun DragHandle(
    modifier: Modifier = Modifier,
    dragHandleVerticalPadding: Dp = 22.dp,
    width: Dp = 32.dp,
    height: Dp = 4.dp,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f),
) {
    Surface(
        modifier = modifier
            .padding(vertical = dragHandleVerticalPadding),
        color = color,
        shape = shape,
    ) {
        Box(
            Modifier
                .size(
                    width = width,
                    height = height,
                ),
        )
    }
}

@Composable
fun AppDragHandle(
    text: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            DragHandle(
                modifier = Modifier.align(Alignment.TopCenter),
                dragHandleVerticalPadding = 8.dp,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .align(Alignment.TopStart),
                text = text,
                style = MaterialTheme.typography.headlineSmall,
            )
            Row(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 8.dp)
                    .align(Alignment.BottomEnd),
                content = actions,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            )
        }
    }
}
