package me.oikvpqya.apps.music.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.oikvpqya.apps.music.room.dao.BlacklistDao
import me.oikvpqya.apps.music.room.dao.FavoriteDao
import me.oikvpqya.apps.music.room.dao.HistoryDao
import me.oikvpqya.apps.music.room.dao.PlayCountDao
import me.oikvpqya.apps.music.room.dao.PlaylistDao
import me.oikvpqya.apps.music.room.dao.QueueDao
import me.oikvpqya.apps.music.room.model.BlacklistEntity
import me.oikvpqya.apps.music.room.model.HistoryEntity
import me.oikvpqya.apps.music.room.model.PlayCountEntity
import me.oikvpqya.apps.music.room.model.PlaylistNameEntity
import me.oikvpqya.apps.music.room.model.PlaylistSongEntity
import me.oikvpqya.apps.music.room.model.SongEntity
import me.oikvpqya.apps.music.room.model.SongWithIdEntity

@Database(
    entities = [
        BlacklistEntity::class,
        HistoryEntity::class,
        PlayCountEntity::class,
        PlaylistNameEntity::class,
        PlaylistSongEntity::class,
        SongEntity::class,
        SongWithIdEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [AppTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun blacklistDao(): BlacklistDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
    abstract fun playCountDao(): PlayCountDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun queueDao(): QueueDao
}
