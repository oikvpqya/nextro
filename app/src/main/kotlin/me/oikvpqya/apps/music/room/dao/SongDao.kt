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
        val tag: Tag.Song,
    )

    @Query(
        value = """
        SELECT tag FROM song
        WHERE mediaId = :mediaId
    """
    )
    fun get(mediaId: Long): Flow<Library.Song.Default?>

    @Query(
        value = """
        SELECT tag FROM song
        WHERE mediaId = :mediaId
    """
    )
    suspend fun getOneShot(mediaId: Long): Library.Song.Default?

    @Query(
        value = """
        SELECT tag FROM song
    """
    )
    fun getAll(): Flow<List<Library.Song.Default>>

    @Query(
        value = """
        INSERT INTO song
        VALUES (:mediaId, :tag)
        ON CONFLICT (mediaId) DO NOTHING
    """
    )
    suspend fun upsert(mediaId: Long, tag: Tag.Song)

    @Query(
        value = """
        DELETE FROM song
        WHERE mediaId = :mediaId
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
