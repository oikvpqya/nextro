package me.oikvpqya.apps.music.room

import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import me.oikvpqya.apps.music.data.AppDatabaseRepository
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
    private val playlistNameDao: PlaylistNameDao,
    private val playlistSongDao: PlaylistSongDao,
    private val queueDao: QueueDao,
    private val songDao: SongDao,
) : AppDatabaseRepository {

    override val favoriteFlow: Flow<List<Library.Song.Default>>
        get() = favoriteDao.getAll()
    override val historyFlow: Flow<List<Library.Song.History>>
        get() = historyDao.getAll()
    override val playlistsFlow: Flow<List<Libraries.Playlist>>
        get() = playlistNameDao.getAll().map { entity ->
            entity.map { name ->
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

    override val songsFlow: Flow<List<Library.Song.Default>>
        get() = songDao.getSongsByASC()

    override suspend fun createPlaylist(name: String) {
        playlistNameDao.insert(name)
    }

    override suspend fun setFavorite(mediaIds: List<Long>) {
        with(appDatabase) {
            useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    mediaIds.forEach { mediaId ->
                        with(favoriteDao) {
                            if (getOneShot(mediaId) != null) {
                                delete(mediaId)
                            } else {
                                upsert(mediaId)
                            }
                        }
                    }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override suspend fun deleteFavorite(mediaIds: List<Long>) = setFavorite(mediaIds)

    override suspend fun setQueues(mediaIds: List<Long>) {
        with(appDatabase) {
            useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    queueDao.deleteAll()
                    mediaIds.forEach { queueDao.insert(it) }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override suspend fun setHistory(mediaIds: List<Long>) {
        with(appDatabase) {
            useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    val epochMilliseconds = Clock.System.now().toEpochMilliseconds()
                    mediaIds.forEach { mediaId ->
                        historyDao.insert(epochMilliseconds, mediaId)
                        playCountDao.upsert(mediaId)
                    }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override fun isFavoriteFlow(mediaId: Long): Flow<Boolean> {
        return favoriteDao.get(mediaId).map { it != null }
    }

    override suspend fun upsertSongs(songs: List<Library.Song>) {
        with(appDatabase) {
            useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    songs.forEach { songDao.upsert(it.tag) }
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }

    override suspend fun deleteSongs() {
        with(appDatabase) {
            useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    songDao.deleteAll()
                }
            }
            // Manually triggers invalidation
            invalidationTracker.refreshAsync()
        }
    }
}
