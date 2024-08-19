package me.oikvpqya.apps.music.ui.media

import androidx.compose.runtime.Composable
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag

@Composable
fun Tag.getArtworkUri(): Any {
    return getArtworkUri(albumId)
}

@Composable
fun getArtworkUri(albumId: Long): Any {
    val artworkProvider = LocalArtworkProvider.current
    return artworkProvider.getArtwork(albumId)
}

@Composable
fun Library.getArtworkUri(): Any {
    return getArtworkUri(tag.albumId)
}
