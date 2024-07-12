package me.oikvpqya.apps.music.room

import androidx.media3.common.MediaItem
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.media3.util.asTag
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag
import me.oikvpqya.apps.music.model.asAlbumTag
import me.oikvpqya.apps.music.model.asArtistTag
import me.oikvpqya.apps.music.room.dao.*
import me.tatarka.inject.annotations.Inject

@Inject
class AppDatabaseRepositoryRoomImpl(
    private val appDatabase: AppDatabase,
    private val favoriteDao: FavoriteDao,
    private val historyDao: HistoryDao,
    private val playCountDao: PlayCountDao,
    private val playlistDao: PlaylistDao,
    private val queueDao: QueueDao
) : AppDatabaseRepository {
    override val favoriteFlow: Flow<List<Library.Song.Default>>
        get() = favoriteDao.getAll()
    override val historyFlow: Flow<List<Library.Song.History>>
        get() = historyDao.getAll()
    override val playlistsFlow: Flow<List<Libraries.Playlist>>
        get() = playlistDao.getAllNames().map { entity ->
            entity.map { (name, _) ->
                // TODO
                Libraries.Playlist(
                    tag = Tag.Default(name = name, albumId = -1L),
                    size = 0
                )
            }
        }
    override val queueFlow: Flow<List<Library.Song.Default>>
        get() = queueDao.getAll()
    override val topAlbumsFlow: Flow<List<Libraries.Album>>
        get() = topSongsFlow.map { songs ->
            songs
                .groupBy {
                    it.tag.albumId
                }
                .map { (_, songs) ->
                    require(songs.isNotEmpty())
                    Libraries.Album(
                        tag = songs.first().tag.asAlbumTag(),
                        size = songs.size
                    )
                }
                .sortedWith { x, y -> (y.size).compareTo(x.size) }
        }
    override val topArtistsFlow: Flow<List<Libraries.Default>>
        get() = topSongsFlow.map { songs ->
            songs
                .groupBy {
                    it.tag.artist
                }
                .map { (_, songs) ->
                    require(songs.isNotEmpty())
                    Libraries.Default(
                        tag = songs.first().tag.asArtistTag(),
                        size = songs.size
                    )
                }
                .sortedWith { x, y -> (y.size).compareTo(x.size) }
        }
    override val topSongsFlow: Flow<List<Library.Song.PlayCount>>
        get() = playCountDao.getAll()

    override suspend fun createPlaylist(name: String) {
        playlistDao.insertName(name)
    }

    override suspend fun setFavorite(songs: List<MediaItem>) {
        with(appDatabase) {
            useWriterConnection {
                it.immediateTransaction {
                    songs.forEach {
                        with(favoriteDao) {
                            val tag = it.asTag()
                            if (getOneShot(tag.mediaId) != null) {
                                delete(tag.mediaId)
                            } else {
                                insert(tag.mediaId, tag)
                            }
                        }
                    }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override suspend fun deleteFavorite(songs: List<MediaItem>) = setFavorite(songs)

    override suspend fun setQueues(songs: List<MediaItem>) {
        with(appDatabase) {
            useWriterConnection {
                it.immediateTransaction {
                    queueDao.deleteAll()
                    songs.forEach {
                        val tag = it.asTag()
                        queueDao.insert(tag.mediaId, tag)
                    }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override suspend fun setHistory(songs: List<MediaItem>) {
        with(appDatabase) {
            useWriterConnection {
                it.immediateTransaction {
                    val epochMilliseconds = Clock.System.now().toEpochMilliseconds()
                    songs.forEach {
                        val tag = it.asTag()
                        historyDao.insert(epochMilliseconds, tag.mediaId, tag)
                        playCountDao.insert(tag.mediaId, tag)
                    }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override fun isFavoriteFlow(song: MediaItem): Flow<Boolean> {
        val mediaId = song.asTag().mediaId
        return favoriteDao.get(mediaId).map { it != null }
    }
}

@Inject
class AppDatabaseRepositoryRoomFake(
    private val appDatabase: AppDatabase,
    private val favoriteDao: FavoriteDao,
    private val historyDao: HistoryDao,
    private val playCountDao: PlayCountDao,
    private val playlistDao: PlaylistDao,
    private val queueDao: QueueDao
) : AppDatabaseRepository {
    override val favoriteFlow: Flow<List<Library.Song.Default>>
        get() = flow {  }
    override val historyFlow: Flow<List<Library.Song>>
        get() = flow {  }
    override val playlistsFlow: Flow<List<Libraries.Playlist>>
        get() = flow {  }
    override val queueFlow: Flow<List<Library.Song.Default>>
        get() = flow {  }
    override val topAlbumsFlow: Flow<List<Libraries.Album>>
        get() = flow {  }
    override val topArtistsFlow: Flow<List<Libraries.Default>>
        get() = flow {  }
    override val topSongsFlow: Flow<List<Library.Song>>
        get() = flow {  }

    override suspend fun createPlaylist(name: String) {
    }

    override suspend fun setFavorite(songs: List<MediaItem>) {
    }

    override suspend fun deleteFavorite(songs: List<MediaItem>) {
    }

    override suspend fun setQueues(songs: List<MediaItem>) {
    }

    override suspend fun setHistory(songs: List<MediaItem>) {
    }

    override fun isFavoriteFlow(song: MediaItem): Flow<Boolean> = flow {  }
}
