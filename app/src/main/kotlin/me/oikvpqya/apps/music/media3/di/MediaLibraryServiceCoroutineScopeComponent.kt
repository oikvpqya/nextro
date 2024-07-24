package me.oikvpqya.apps.music.media3.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

typealias MediaLibraryServiceCoroutineScope = CoroutineScope

interface MediaLibraryServiceCoroutineScopeComponent {

    val coroutineScope: MediaLibraryServiceCoroutineScope

    @Provides
    @MediaLibraryServiceScope
    fun provideMediaLibraryServiceCoroutineScope(): MediaLibraryServiceCoroutineScope {
        return CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}
