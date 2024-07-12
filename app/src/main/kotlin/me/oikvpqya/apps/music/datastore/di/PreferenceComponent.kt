package me.oikvpqya.apps.music.datastore.di

import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.datastore.PreferenceRepositoryImpl
import me.tatarka.inject.annotations.Provides

interface PreferenceComponent {

    val preferenceRepository: PreferenceRepository

    @ApplicationScope
    @Provides
    fun bindPreferenceRepository(bind: PreferenceRepositoryImpl): PreferenceRepository = bind
}
