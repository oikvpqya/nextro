package me.oikvpqya.apps.music.feature.library.di

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.library.route.LibraryRouteFactory
import me.oikvpqya.apps.music.feature.library.route.AlbumsRouteFactory
import me.oikvpqya.apps.music.feature.library.route.ArtistsRouteFactory
import me.oikvpqya.apps.music.feature.library.route.HomeRouteFactory
import me.oikvpqya.apps.music.feature.library.route.PlaylistsRouteFactory
import me.oikvpqya.apps.music.feature.library.route.SongsRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface LibraryComponent :
    AlbumsComponent,
    ArtistsComponent,
    HomeComponent,
    SongsComponent,
    PlaylistsComponent

interface AlbumsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindAlbumsRouteFactory(bind: AlbumsRouteFactory): LibraryRouteFactory = bind
}

interface ArtistsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindArtistsRouteFactory(bind: ArtistsRouteFactory): LibraryRouteFactory = bind
}

interface HomeComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindHomeRouteFactory(bind: HomeRouteFactory): LibraryRouteFactory = bind
}

interface PlaylistsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindPlaylistsRouteFactory(bind: PlaylistsRouteFactory): LibraryRouteFactory = bind
}

interface SongsComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindSongsRouteFactory(bind: SongsRouteFactory): LibraryRouteFactory = bind
}
