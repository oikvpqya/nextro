package me.oikvpqya.apps.music.app.di

import android.content.Context
import me.oikvpqya.apps.music.coil.di.CoilComponent
import me.oikvpqya.apps.music.datastore.di.PreferenceComponent
import me.oikvpqya.apps.music.media3.di.AppMediaControllerComponent
import me.oikvpqya.apps.music.mediastore.di.AppMediaStoreComponent
import me.oikvpqya.apps.music.room.di.AppDatabaseComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

@Scope
annotation class ApplicationScope

@Component
@ApplicationScope
abstract class ApplicationComponent(
    @get:Provides val context: Context,
) : CoilComponent,
    AppMediaStoreComponent,
    AppDatabaseComponent,
    PreferenceComponent,
    AppMediaControllerComponent

interface ApplicationComponentProvider {
    val component: ApplicationComponent
}

val Context.applicationComponent: ApplicationComponent
    get() = (applicationContext as ApplicationComponentProvider).component
