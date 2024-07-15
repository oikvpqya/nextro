package me.oikvpqya.apps.music.media3.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.oikvpqya.apps.music.app.di.ApplicationComponent
import me.oikvpqya.apps.music.media3.AppMediaLibraryService
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

@Scope
annotation class MediaLibraryServiceScope

typealias MediaLibraryServiceCoroutineScope = CoroutineScope

@MediaLibraryServiceScope
@Component
abstract class AppMediaLibraryServiceComponent(
    @get:Provides val service: AppMediaLibraryService,
    @Component val applicationComponent: ApplicationComponent,
) : MediaLibrarySessionComponent,
    MediaLibraryServiceCoroutineScopeComponent

interface MediaLibraryServiceCoroutineScopeComponent {

    val coroutineScope: MediaLibraryServiceCoroutineScope

    @Provides
    @MediaLibraryServiceScope
    fun provideMediaLibraryServiceCoroutineScope(): MediaLibraryServiceCoroutineScope {
        return CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}
