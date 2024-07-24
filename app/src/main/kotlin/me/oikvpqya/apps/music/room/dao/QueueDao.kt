package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library

@Dao
interface QueueDao {

    @Entity(
        tableName = "queue",
    )
    data class QueueEntity(
        val mediaId: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L
    )

    @Query(
        value = """
        SELECT song.tag FROM song, queue
        WHERE song.mediaId = queue.mediaId
    """
    )
    fun getAll(): Flow<List<Library.Song.Default>>

    @Query(
        value = """
        INSERT INTO queue (mediaId)
        VALUES (:mediaId)
    """
    )
    suspend fun insert(mediaId: Long)

    @Query(
        value = """
        DELETE FROM queue
    """
    )
    suspend fun deleteAll()
}
