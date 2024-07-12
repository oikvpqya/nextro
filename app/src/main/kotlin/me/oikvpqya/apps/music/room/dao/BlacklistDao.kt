package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.room.model.BlacklistEntity

@Dao
interface BlacklistDao {

    @Query(
        value = """
        INSERT INTO blacklist
        VALUES (:path) ON CONFLICT (path) DO NOTHING
    """
    )
    suspend fun insert(path: String)

    @Query(
        value = """
        SELECT path FROM blacklist
    """
    )
    fun getAll(): Flow<List<String>>

    @Query(
        value = """
        DELETE FROM blacklist
        WHERE path = :path
    """
    )
    suspend fun delete(path: String)
}
