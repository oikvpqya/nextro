package me.oikvpqya.apps.music.feature.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    readStoragePermissionState: PermissionState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.safeGesturesPadding()
    ) {
        val textToShow = if (readStoragePermissionState.status.shouldShowRationale) {
            // If the user has denied the permission but the rationale can be shown,
            // then gently explain why the app requires this permission
            "Read storage permission is important for this app. Please grant the permission."
        } else {
            // If it's the first time the user lands on this feature, or the user
            // doesn't want to be asked again for this permission, explain that the
            // permission is required
            "Read storage permission permission required for this feature to be available. " +
                    "Please grant the permission"
        }
        Text(textToShow)
        Button(onClick = { readStoragePermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }
}
