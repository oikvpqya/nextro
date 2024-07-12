package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag

@Dao
interface QueueDao {
    @Query(
        value = """
        SELECT tag FROM song_with_id;
    """
    )
    fun getAll(): Flow<List<Library.Song.Default>>

    @Query(
        value = """
        INSERT INTO song_with_id (mediaId, tag)
        VALUES (:mediaId, :tag)
    """
    )
    suspend fun insert(mediaId: Long, tag: Tag.Song)

    @Query(
        value = """
        DELETE FROM song_with_id
    """
    )
    suspend fun deleteAll()
}
