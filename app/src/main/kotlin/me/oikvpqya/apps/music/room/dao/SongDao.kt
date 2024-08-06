package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
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
    """
    )
    fun getAll(): Flow<List<Library.Song.Default>>

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
