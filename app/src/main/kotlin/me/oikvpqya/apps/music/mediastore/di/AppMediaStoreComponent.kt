package me.oikvpqya.apps.music.mediastore.di

import android.content.ContentResolver
import android.content.Context
import me.oikvpqya.apps.music.app.di.ApplicationCoroutineScope
import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.MediaStoreRepository
import me.oikvpqya.apps.music.data.MediaSynchronizer
import me.oikvpqya.apps.music.mediastore.MediaStoreRepositoryImpl
import me.oikvpqya.apps.music.mediastore.MediaSynchronizerImpl
import me.tatarka.inject.annotations.Provides

interface AppMediaStoreComponent {

    @ApplicationScope
    @Provides
    fun provideContentResolver(application: Context): ContentResolver {
        return application.contentResolver
    }

    @ApplicationScope
    @Provides
    fun provideMediaSynchronizer(
        coroutineScope: ApplicationCoroutineScope,
        mediaStoreRepository: MediaStoreRepository,
        appDatabaseRepository: AppDatabaseRepository,
    ): MediaSynchronizer {
        return MediaSynchronizerImpl(
            coroutineScope = coroutineScope,
            mediaStoreRepository = mediaStoreRepository,
            appDatabaseRepository = appDatabaseRepository,
        )
    }

    @ApplicationScope
    @Provides
    fun bindMediaStoreRepository(bind: MediaStoreRepositoryImpl): MediaStoreRepository = bind
}
