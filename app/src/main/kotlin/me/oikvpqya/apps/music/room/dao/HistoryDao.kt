package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library

@Dao
interface HistoryDao {

    @Entity(
        tableName = "history",
    )
    data class HistoryEntity(
        val timestamp: Long,
        val mediaId: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
    )

    @Query(
        value = """
        SELECT song.*, history.timestamp FROM song, history
        WHERE song.mediaId = history.mediaId
        ORDER BY timestamp DESC
    """
    )
    fun getAll(): Flow<List<Library.Song.History>>

    @Query(
        value = """
        SELECT song.*, distinct_history.timestamp FROM song, (
            SELECT DISTINCT history.mediaId, history.timestamp FROM history
        ) AS distinct_history
        WHERE song.mediaId = distinct_history.mediaId
        ORDER BY distinct_history.timestamp DESC
    """
    )
    fun getAllDistinctly(): Flow<List<Library.Song.History>>

    @Query(
        value = """
        INSERT INTO history (timestamp, mediaId)
        VALUES (:timestamp, :mediaId)
    """
    )
    suspend fun insert(timestamp: Long, mediaId: Long)

    @Query(
        value = """
        DELETE FROM history
    """
    )
    suspend fun deleteAll()
}
