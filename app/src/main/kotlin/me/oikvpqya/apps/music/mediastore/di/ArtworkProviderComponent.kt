package me.oikvpqya.apps.music.mediastore.di

import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.ArtworkProvider
import me.oikvpqya.apps.music.mediastore.ArtworkProviderImpl
import me.tatarka.inject.annotations.Provides

interface ArtworkProviderComponent {

    @ApplicationScope
    @Provides
    fun bindArtworkProvider(bind: ArtworkProviderImpl): ArtworkProvider = bind
}
