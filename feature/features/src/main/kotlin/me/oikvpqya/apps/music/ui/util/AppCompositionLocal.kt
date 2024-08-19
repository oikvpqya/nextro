package me.oikvpqya.apps.music.ui.util

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppSnackbarHandler = staticCompositionLocalOf<AppSnackbarHandler> {
    error("CompositionLocal SnackbarHandler not present")
}

val LocalWindowSizeClass = staticCompositionLocalOf<WindowSizeClass> {
    error("CompositionLocal WindowSizeClass not present")
}
