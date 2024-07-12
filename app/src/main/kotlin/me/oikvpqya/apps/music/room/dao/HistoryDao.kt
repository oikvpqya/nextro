package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag

@Dao
interface HistoryDao {
    @Query(
        value = """
        SELECT tag, timestamp FROM history
        ORDER BY timestamp DESC
    """
    )
    fun getAll(): Flow<List<Library.Song.History>>

    @Query(
        value = """
        SELECT tag, timestamp FROM (
            SELECT DISTINCT mediaId, tag, timestamp FROM history
        )
        ORDER BY timestamp DESC
    """
    )
    fun getAllDistinctly(): Flow<List<Library.Song.History>>

    @Query(
        value = """
        INSERT INTO history (timestamp, mediaId, tag)
        VALUES (:timestamp, :mediaId, :tag)
    """
    )
    suspend fun insert(timestamp: Long, mediaId: Long, tag: Tag.Song)

    @Query(
        value = """
        DELETE FROM history
    """
    )
    suspend fun deleteAll()
}
