package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.oikvpqya.compose.fastscroller.ScrollbarAdapter
import io.github.oikvpqya.compose.fastscroller.VerticalScrollbar
import io.github.oikvpqya.compose.fastscroller.material3.defaultMaterialScrollbarStyle

@Composable
fun FastScrollerScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Box(
        modifier = modifier,
    ) {
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.TopEnd),
            adapter = adapter,
            interactionSource = interactionSource,
            style = defaultMaterialScrollbarStyle(),
            enablePressToScroll = false,
        )
    }
}
