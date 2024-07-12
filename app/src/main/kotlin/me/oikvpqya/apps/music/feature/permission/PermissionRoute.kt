package me.oikvpqya.apps.music.feature.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRoute(
    readStoragePermissionState: PermissionState,
    modifier: Modifier = Modifier,
) {
    PermissionScreen(
        readStoragePermissionState = readStoragePermissionState,
        modifier = modifier
    )
}
