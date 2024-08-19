package me.oikvpqya.apps.music.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.media.getArtworkUri
import kotlin.random.Random

@Composable
fun RandomTiledLibraryContainer(
    items: List<Library.Song>,
    onItemClick: (List<Library.Song>, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    RandomTile(
        items = if (items.size != 8) {
            items.take(8).let {
                if (it.size < 8) {
                    val list = buildList {
                        for(i in it.size until 8) {
                            add(null)
                        }
                    }
                    it + list
                } else it
            }
        } else items,
        onItemClick = { mediaId ->
            val index = items.indexOfFirst { mediaId == it.tag.mediaId }.coerceAtLeast(0)
            onItemClick(items, index)
        },
        modifier = modifier.padding(8.dp)
    )
}

@Composable
private fun RandomTile(
    items: List<Library.Song?>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    require(items.size == 8)

    val isLargeContainerFirst = rememberSaveable { Random.nextBoolean() }
    val isLargeItemFirst = rememberSaveable { Random.nextBoolean() }
    BoxWithConstraints(
        modifier = modifier
    ) {
        val padding = 16.dp
        val width = (maxWidth - padding * 4) / 5
        Row(
            horizontalArrangement = Arrangement.spacedBy(padding)
        ) {
            LargeContainer(
                width = width,
                items = items.subList(1, 3),
                onItemClick = onItemClick,
                padding = padding,
                isLargeItemFirst = true
            )
            if (isLargeContainerFirst) {
                LargeContainer(
                    width = width,
                    items = items.subList(3, 5) + listOf(items[0]),
                    onItemClick = onItemClick,
                    padding = padding,
                    isLargeItemFirst = isLargeItemFirst
                )
                SmallContainer(
                    width = width,
                    items = items.subList(5, 8),
                    onItemClick = onItemClick,
                    padding = padding,
                )
            } else {
                SmallContainer(
                    width = width,
                    items = items.subList(3, 6),
                    onItemClick = onItemClick,
                    padding = padding,
                )
                LargeContainer(
                    width = width,
                    items = items.subList(6, 8) + listOf(items[0]),
                    onItemClick = onItemClick,
                    padding = padding,
                    isLargeItemFirst = isLargeItemFirst
                )
            }
        }
    }
}

@Composable
private fun LargeContainer(
    width: Dp,
    items: List<Library.Song?>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 16.dp,
    isLargeItemFirst: Boolean = Random.nextBoolean()
) {
    require(items.size == 2 || items.size == 3)

    @Composable
    fun SmallItems() {
        Row(
            horizontalArrangement = Arrangement.spacedBy(padding)
        ) {
            val uri0 = items[0]?.getArtworkUri()
            if (uri0 != null) {
                ImageContainerSample(
                    data = uri0,
                    size = width,
                    onClickEnable = true,
                    onClick = {
                        val mediaId = items[0]?.tag?.mediaId
                        if (mediaId != null) {
                            onItemClick(mediaId)
                        }
                    },
                )
            } else {
                ImageContainerSample(
                    size = width
                )
            }
            val uri1 = items[1]?.getArtworkUri()
            if (uri1 != null) {
                ImageContainerSample(
                    data = uri1,
                    size = width,
                    onClickEnable = true,
                    onClick = {
                        val mediaId = items[1]?.tag?.mediaId
                        if (mediaId != null) {
                            onItemClick(mediaId)
                        }
                    }
                )
            } else {
                ImageContainerSample(
                    size = width
                )
            }
        }
    }

    @Composable
    fun LargeItem() {
        if (items.size == 3) {
            val uri = items[2]?.getArtworkUri()
            if (uri != null) {
                ImageContainerSample(
                    data = uri,
                    size = width * 2 + padding,
                    onClickEnable = true,
                    onClick = {
                        val mediaId = items[2]?.tag?.mediaId
                        if (mediaId != null) {
                            onItemClick(mediaId)
                        }
                    },
                )
            } else {
                ImageContainerSample(
                    size = width * 2 + padding
                )
            }
        } else {
            SuggestionCard(
                onClick = {},
                modifier = Modifier.size(width * 2 + padding),
            ) {
                SuggestionMessage()
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(padding)
    ) {
        if (isLargeItemFirst) {
            LargeItem()
            SmallItems()
        } else {
            SmallItems()
            LargeItem()
        }
    }
}

@Composable
private fun SmallContainer(
    width: Dp,
    items: List<Library.Song?>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 16.dp,
) {
    require(items.size == 3)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(padding)
    ) {
        val uri0 = items[0]?.getArtworkUri()
        val uri1 = items[1]?.getArtworkUri()
        val uri2 = items[2]?.getArtworkUri()
        if (uri0 != null) {
            ImageContainerSample(
                data = uri0,
                size = width,
                onClickEnable = true,
                onClick = {
                    val mediaId = items[0]?.tag?.mediaId
                    if (mediaId != null) {
                        onItemClick(mediaId)
                    }
                }
            )
        } else {
            ImageContainerSample(
                size = width
            )
        }
        if (uri1 != null) {
            ImageContainerSample(
                data = uri1,
                size = width,
                onClickEnable = true,
                onClick = {
                    val mediaId = items[1]?.tag?.mediaId
                    if (mediaId != null) {
                        onItemClick(mediaId)
                    }
                }
            )
        } else {
            ImageContainerSample(
                size = width
            )
        }
        if (uri2 != null) {
            ImageContainerSample(
                data = uri2,
                size = width,
                onClickEnable = true,
                onClick = {
                    val mediaId = items[2]?.tag?.mediaId
                    if (mediaId != null) {
                        onItemClick(mediaId)
                    }
                }
            )
        } else {
            ImageContainerSample(
                size = width
            )
        }
    }
}


@Composable
fun SuggestionMessage(
    modifier: Modifier = Modifier,
) {
    val textStyle = MaterialTheme.typography.headlineLarge
        .copy(
            lineBreak = LineBreak.Heading
        )
    Text(
        modifier = modifier.fillMaxSize().padding(8.dp),
        text = "New Music Mix",
        style = textStyle,
    )
}

@Composable
fun SuggestionCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Surface(
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val width = placeable.width
                val height = placeable.height
                val size = maxOf(width, height)
                layout(size, size) {
                    val x = (size - placeable.width) / 2
                    val y = (size - placeable.height) / 2
                    placeable.placeRelative(x, y)
                }
            },
            onClick = onClick,
            content = content,
        )
    }
}

@Preview
@Composable
fun SuggestionMessagePreview() {
    SuggestionCard(
        onClick = {},
        modifier = Modifier.size(150.dp)
    ) {
        SuggestionMessage()
    }
}
