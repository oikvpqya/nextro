package me.oikvpqya.apps.music.feature.songs

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SongsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindSongsRouteFactory(bind: SongsRouteFactory): LibraryRouteFactory = bind
}
