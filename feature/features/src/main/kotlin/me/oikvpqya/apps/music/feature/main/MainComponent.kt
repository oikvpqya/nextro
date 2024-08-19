package me.oikvpqya.apps.music.feature.main

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.RootRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface MainComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindMainRouteFactory(bind: MainRouteFactory): RootRouteFactory = bind
}
