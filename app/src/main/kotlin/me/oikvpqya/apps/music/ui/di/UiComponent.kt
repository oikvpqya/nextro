package me.oikvpqya.apps.music.ui.di

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.albums.AlbumsComponent
import me.oikvpqya.apps.music.feature.artists.ArtistsComponent
import me.oikvpqya.apps.music.feature.home.HomeComponent
import me.oikvpqya.apps.music.feature.main.MainComponent
import me.oikvpqya.apps.music.feature.playlists.PlaylistsComponent
import me.oikvpqya.apps.music.feature.preference.PreferenceComponent
import me.oikvpqya.apps.music.feature.root.AppContent
import me.oikvpqya.apps.music.feature.root.AppContentImpl
import me.oikvpqya.apps.music.feature.songs.SongsComponent
import me.tatarka.inject.annotations.Provides

interface UiComponent :
    PreferenceComponent,
    MainComponent,
    AlbumsComponent,
    ArtistsComponent,
    HomeComponent,
    SongsComponent,
    PlaylistsComponent
{

    val appContent: AppContent

    @Provides
    @ActivityScope
    fun bindAppContent(bind: AppContentImpl): AppContent = bind
}
