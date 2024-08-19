package me.oikvpqya.apps.music.ui.media

import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf
import me.oikvpqya.apps.music.data.AppMediaHandler
import me.oikvpqya.apps.music.data.AppMediaInfo
import me.oikvpqya.apps.music.data.ArtworkProvider

val LocalArtworkProvider = staticCompositionLocalOf<ArtworkProvider> {
    error("CompositionLocal ArtworkProvider not present.")
}

val LocalMediaHandlerState = staticCompositionLocalOf<State<AppMediaHandler?>> {
    error("CompositionLocal AppMediaHandler not present.")
}

val LocalMediaInfoState = staticCompositionLocalOf<State<AppMediaInfo>> {
    error("CompositionLocal AppMediaInfo not present.")
}
