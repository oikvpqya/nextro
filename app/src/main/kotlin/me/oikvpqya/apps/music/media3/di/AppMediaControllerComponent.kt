package me.oikvpqya.apps.music.media3.di

import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.AppMediaController
import me.oikvpqya.apps.music.media3.AppMediaControllerImpl
import me.tatarka.inject.annotations.Provides

interface AppMediaControllerComponent {

    @ApplicationScope
    @Provides
    fun bindAppMediaController(bind: AppMediaControllerImpl): AppMediaController = bind
}
