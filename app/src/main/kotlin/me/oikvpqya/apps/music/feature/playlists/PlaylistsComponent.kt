package me.oikvpqya.apps.music.feature.playlists

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface PlaylistsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindPlaylistsRouteFactory(bind: PlaylistsRouteFactory): LibraryRouteFactory = bind
}
