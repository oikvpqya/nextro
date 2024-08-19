package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag

@Dao
interface SongDao {

    @Entity(
        tableName = "song",
    )
    data class SongEntity(
        @PrimaryKey
        val mediaId: Long,
        val album: String,
        val artist: String,
        val title: String,
        val albumArtist: String,
        val composer: String,
        val data: String,
        val dateModified: Long,
        val duration: Long,
        val genre: String,
        val trackNumber: Long,
        val year: Long,
        val albumId: Long,
    )

    @Query(
        value = """
        SELECT song.* FROM song
        WHERE song.mediaId = :mediaId
    """
    )
    fun get(mediaId: Long): Flow<Library.Song.Default?>

    @Query(
        value = """
        SELECT song.* FROM song
        WHERE song.mediaId = :mediaId
    """
    )
    suspend fun getOneShot(mediaId: Long): Library.Song.Default?

    @Query(
        value = """
        SELECT song.* FROM song
        ORDER BY song.title ASC
    """
    )
    fun getSongsByASC(): Flow<List<Library.Song.Default>>

    @Query(
        value = """
        SELECT song.albumArtist, song.genre, song.year, song.albumId, song.album AS name, count(*) AS size
        FROM song
        GROUP BY name
        ORDER BY name ASC
    """
    )
    fun getAlbumsByASC(): Flow<List<Libraries.Album>>

    @Query(
        value = """
        SELECT song.albumId, song.artist AS name, count(*) AS size
        FROM song
        GROUP BY name
        ORDER BY name ASC
    """
    )
    fun getArtistByASC(): Flow<List<Libraries.Default>>

    @Query(
        value = """
        INSERT INTO song
        VALUES (:mediaId, :album, :artist, :title, :albumArtist, :composer, :data, :dateModified,
            :duration, :genre, :trackNumber, :year, :albumId)
        ON CONFLICT (song.mediaId) DO NOTHING
    """
    )
    suspend fun upsert(
        mediaId: Long,
        album: String,
        artist: String,
        title: String,
        albumArtist: String,
        composer: String,
        data: String,
        dateModified: Long,
        duration: Long,
        genre: String,
        trackNumber: Long,
        year: Long,
        albumId: Long,
    )

    @Query(
        value = """
        DELETE FROM song
        WHERE song.mediaId = :mediaId
    """
    )
    suspend fun delete(mediaId: Long)

    @Query(
        value = """
        DELETE FROM song
    """
    )
    suspend fun deleteAll()
}

suspend fun SongDao.upsert(tag: Tag.Song) {
    with(tag) { upsert(mediaId, album, artist, title, albumArtist, composer, data, dateModified,
            duration, genre, trackNumber, year, albumId) }
}
