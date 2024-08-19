package me.oikvpqya.apps.music.ui.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Stable

@Stable
class AppSnackbarHandler(
    val state: SnackbarHostState
) {
    suspend fun onShowSnackbar(
        message: String,
        action: String?
    ) = state.showSnackbar(
        message = message,
        actionLabel = action,
        duration = SnackbarDuration.Short,
    ) == SnackbarResult.ActionPerformed
}
