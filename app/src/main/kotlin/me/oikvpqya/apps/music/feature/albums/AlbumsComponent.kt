package me.oikvpqya.apps.music.feature.albums

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface AlbumsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindAlbumsRouteFactory(bind: AlbumsRouteFactory): LibraryRouteFactory = bind
}
