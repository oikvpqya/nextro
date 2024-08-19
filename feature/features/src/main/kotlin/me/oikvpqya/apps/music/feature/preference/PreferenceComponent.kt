package me.oikvpqya.apps.music.feature.preference

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.RootRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface PreferenceComponent {

    @IntoSet
    @Provides
    @ActivityScope
    fun bindPreferenceRouteFactory(bind: PreferenceRouteFactory): RootRouteFactory = bind
}
