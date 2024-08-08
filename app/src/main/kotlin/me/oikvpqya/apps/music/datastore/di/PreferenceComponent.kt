package me.oikvpqya.apps.music.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.datastore.PreferenceRepositoryImpl
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toPath

interface PreferenceComponent {

    val preferenceRepository: PreferenceRepository

    @ApplicationScope
    @Provides
    fun bindPreferenceRepository(bind: PreferenceRepositoryImpl): PreferenceRepository = bind

    @ApplicationScope
    @Provides
    fun provideDataStore(
        application: Context,
    ): DataStore<Preferences> {
        val file = application.filesDir.resolve("settings.preferences_pb")
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { file.absolutePath.toPath() }
        )
    }
}
