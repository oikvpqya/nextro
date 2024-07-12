package me.oikvpqya.apps.music.room.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import me.oikvpqya.apps.music.app.di.ApplicationScope
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.room.AppDatabase
import me.oikvpqya.apps.music.room.AppDatabaseRepositoryRoomFake
import me.oikvpqya.apps.music.room.AppDatabaseRepositoryRoomImpl
import me.oikvpqya.apps.music.room.AppTypeConverter
import me.oikvpqya.apps.music.room.dao.*
import me.tatarka.inject.annotations.Provides

interface AppDatabaseComponent {

    val appDatabaseRepository: AppDatabaseRepository

    @ApplicationScope
    @Provides
    fun bindAppDatabaseRepository(bind: AppDatabaseRepositoryRoomImpl): AppDatabaseRepository = bind
//    fun bindAppDatabaseRepository(bind: AppDatabaseRepositoryRoomFake): AppDatabaseRepository = bind

    @ApplicationScope
    @Provides
    fun provideRoomDatabase(
        application: Context
    ): AppDatabase {
        val path = application.getDatabasePath("room").absolutePath
        return Room.databaseBuilder<AppDatabase>(
            application,
            path
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
//            .allowMainThreadQueries()
            .addTypeConverter(AppTypeConverter())
            .build()
    }

    @ApplicationScope
    @Provides
    fun provideBlacklistDao(
        db: AppDatabase
    ): BlacklistDao {
        return db.blacklistDao()
    }

    @ApplicationScope
    @Provides
    fun provideFavoriteDao(
        db: AppDatabase
    ): FavoriteDao {
        return db.favoriteDao()
    }

    @ApplicationScope
    @Provides
    fun provideHistoryDao(
        db: AppDatabase
    ): HistoryDao {
        return db.historyDao()
    }

    @ApplicationScope
    @Provides
    fun providePlayCountDao(
        db: AppDatabase
    ): PlayCountDao {
        return db.playCountDao()
    }

    @ApplicationScope
    @Provides
    fun providePlaylistDao(
        db: AppDatabase
    ): PlaylistDao {
        return db.playlistDao()
    }

    @ApplicationScope
    @Provides
    fun provideQueueDao(
        db: AppDatabase
    ): QueueDao {
        return db.queueDao()
    }
}
