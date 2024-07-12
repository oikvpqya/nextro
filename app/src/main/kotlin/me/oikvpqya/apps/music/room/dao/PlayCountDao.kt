package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag

@Dao
interface PlayCountDao {
    @Query(
        value = """
        SELECT tag, count FROM play_count
        ORDER BY count DESC
    """
    )
    fun getAll(): Flow<List<Library.Song.PlayCount>>

    @Query(
        value = """
        INSERT INTO play_count
        VALUES (:mediaId, 1, :tag)
        ON CONFLICT (mediaId) DO UPDATE SET count = count + 1
    """
    )
    suspend fun insert(mediaId: Long, tag: Tag.Song)

    @Query(
        value = """
        DELETE FROM play_count
    """
    )
    suspend fun deleteAll()
}
