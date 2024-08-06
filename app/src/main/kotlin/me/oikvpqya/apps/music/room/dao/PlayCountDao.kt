package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library

@Dao
interface PlayCountDao {

    @Entity(
        tableName = "play_count",
    )
    data class PlayCountEntity(
        @PrimaryKey
        val mediaId: String,
        val count: Long,
    )

    @Query(
        value = """
        SELECT song.*, play_count.count FROM song, play_count
        WHERE song.mediaId = play_count.mediaId
        ORDER BY count DESC
    """
    )
    fun getAll(): Flow<List<Library.Song.PlayCount>>

    @Query(
        value = """
        INSERT INTO play_count
        VALUES (:mediaId, 1)
        ON CONFLICT (play_count.mediaId) DO UPDATE SET count = play_count.count + 1
    """
    )
    suspend fun upsert(mediaId: Long)

    @Query(
        value = """
        DELETE FROM play_count
    """
    )
    suspend fun deleteAll()
}
