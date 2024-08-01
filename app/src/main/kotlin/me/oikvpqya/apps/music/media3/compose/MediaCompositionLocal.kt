package me.oikvpqya.apps.music.media3.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf
import me.oikvpqya.apps.music.data.AppMediaHandler
import me.oikvpqya.apps.music.data.AppMediaInfo

val LocalMediaHandlerState = staticCompositionLocalOf<State<AppMediaHandler?>> {
    error("CompositionLocal AppMediaHandler not present.")
}

val LocalMediaInfoState = staticCompositionLocalOf<State<AppMediaInfo>> {
    error("CompositionLocal AppMediaInfo not present.")
}
