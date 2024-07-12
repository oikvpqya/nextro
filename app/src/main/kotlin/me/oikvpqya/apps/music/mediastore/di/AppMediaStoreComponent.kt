package me.oikvpqya.apps.music.mediastore.di

import android.content.ContentResolver
import android.content.Context
import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.MediaStoreRepository
import me.oikvpqya.apps.music.mediastore.MediaStoreRepositoryImpl
import me.tatarka.inject.annotations.Provides

interface AppMediaStoreComponent {

    @ApplicationScope
    @Provides
    fun provideContentResolver(application: Context): ContentResolver =
        application.contentResolver

    @ApplicationScope
    @Provides
    fun bindMediaStoreRepository(bind: MediaStoreRepositoryImpl): MediaStoreRepository = bind
}
