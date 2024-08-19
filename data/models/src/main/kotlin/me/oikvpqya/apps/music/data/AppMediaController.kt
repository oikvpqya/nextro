package me.oikvpqya.apps.music.data

import kotlinx.coroutines.flow.Flow

interface AppMediaController {
    val mediaHandlerFlow: Flow<AppMediaHandler>
    val mediaInfoFlow: Flow<AppMediaInfo>
}
