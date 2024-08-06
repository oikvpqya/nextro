package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library

@Dao
interface FavoriteDao {

    @Entity(
        tableName = "favorite",
    )
    data class FavoriteEntity(
        @PrimaryKey
        val mediaId: Long,
    )

    @Query(
        value = """
        SELECT song.* FROM song, favorite
        WHERE song.mediaId = favorite.mediaId
        AND song.mediaId = :mediaId
    """
    )
    fun get(mediaId: Long): Flow<Library.Song.Default?>

    @Query(
        value = """
        SELECT song.* FROM song, favorite
        WHERE song.mediaId = favorite.mediaId
        AND song.mediaId = :mediaId
    """
    )
    suspend fun getOneShot(mediaId: Long): Library.Song.Default?

    @Query(
        value = """
        SELECT song.* FROM song, favorite
        WHERE song.mediaId = favorite.mediaId
    """
    )
    fun getAll(): Flow<List<Library.Song.Default>>

    @Query(
        value = """
        INSERT INTO favorite
        VALUES (:mediaId)
        ON CONFLICT (favorite.mediaId) DO NOTHING
    """
    )
    suspend fun upsert(mediaId: Long)

    @Query(
        value = """
        DELETE FROM favorite
        WHERE favorite.mediaId = :mediaId
    """
    )
    suspend fun delete(mediaId: Long)

    @Query(
        value = """
        DELETE FROM favorite
    """
    )
    suspend fun deleteAll()
}
