package me.oikvpqya.apps.music.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.oikvpqya.apps.music.room.dao.BlacklistDao
import me.oikvpqya.apps.music.room.dao.FavoriteDao
import me.oikvpqya.apps.music.room.dao.HistoryDao
import me.oikvpqya.apps.music.room.dao.PlayCountDao
import me.oikvpqya.apps.music.room.dao.PlaylistNameDao
import me.oikvpqya.apps.music.room.dao.PlaylistSongDao
import me.oikvpqya.apps.music.room.dao.QueueDao
import me.oikvpqya.apps.music.room.dao.SongDao

@Database(
    entities = [
        BlacklistDao.BlacklistEntity::class,
        HistoryDao.HistoryEntity::class,
        PlayCountDao.PlayCountEntity::class,
        PlaylistNameDao.PlaylistNameEntity::class,
        PlaylistSongDao.PlaylistSongEntity::class,
        SongDao.SongEntity::class,
        QueueDao.QueueEntity::class,
        FavoriteDao.FavoriteEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(value = [AppTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun blacklistDao(): BlacklistDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
    abstract fun playCountDao(): PlayCountDao
    abstract fun playlistNameDao(): PlaylistNameDao
    abstract fun playlistSongDao(): PlaylistSongDao
    abstract fun queueDao(): QueueDao
    abstract fun songDao(): SongDao
}
