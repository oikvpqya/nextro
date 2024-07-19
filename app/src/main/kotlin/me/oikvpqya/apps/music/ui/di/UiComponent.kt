package me.oikvpqya.apps.music.ui.di

import me.oikvpqya.apps.music.app.di.ActivityScope
import me.oikvpqya.apps.music.feature.library.di.LibraryComponent
import me.oikvpqya.apps.music.feature.main.MainComponent
import me.oikvpqya.apps.music.feature.preference.PreferenceComponent
import me.oikvpqya.apps.music.feature.root.AppContent
import me.oikvpqya.apps.music.feature.root.AppContentImpl
import me.tatarka.inject.annotations.Provides

interface UiComponent :
    PreferenceComponent,
    MainComponent,
    LibraryComponent {

    val appContent: AppContent

    @Provides
    @ActivityScope
    fun bindAppContent(bind: AppContentImpl): AppContent = bind
}
