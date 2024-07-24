package me.oikvpqya.apps.music.app.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

typealias ApplicationCoroutineScope = CoroutineScope

interface ApplicationCoroutineScopeComponent {

    @ApplicationScope
    @Provides
    fun provideMediaLibraryServiceCoroutineScope(): ApplicationCoroutineScope {
        return CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}
