package me.oikvpqya.apps.music.feature.artists

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ArtistsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindArtistsRouteFactory(bind: ArtistsRouteFactory): LibraryRouteFactory = bind
}
