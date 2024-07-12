package me.oikvpqya.apps.music.media3.di

import android.app.Activity
import me.oikvpqya.apps.music.app.di.ApplicationComponent
import me.oikvpqya.apps.music.media3.AppMediaLibraryService
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

@Scope
annotation class MediaLibraryServiceScope

@MediaLibraryServiceScope
@Component
abstract class AppMediaLibraryServiceComponent(
    @get:Provides val service: AppMediaLibraryService,
    @Component val applicationComponent: ApplicationComponent,
)
