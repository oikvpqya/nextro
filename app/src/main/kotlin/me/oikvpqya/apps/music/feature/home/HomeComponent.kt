package me.oikvpqya.apps.music.feature.home

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.LibraryRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface HomeComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindHomeRouteFactory(bind: HomeRouteFactory): LibraryRouteFactory = bind
}
