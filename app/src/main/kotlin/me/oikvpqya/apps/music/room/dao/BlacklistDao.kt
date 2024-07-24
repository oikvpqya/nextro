package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlacklistDao {

    @Entity(
        tableName = "blacklist",
    )
    data class BlacklistEntity(
        @PrimaryKey
        val path: String,
    )

    @Query(
        value = """
        INSERT INTO blacklist
        VALUES (:path) ON CONFLICT (blacklist.path) DO NOTHING
    """
    )
    suspend fun insert(path: String)

    @Query(
        value = """
        SELECT blacklist.path FROM blacklist
    """
    )
    fun getAll(): Flow<List<String>>

    @Query(
        value = """
        DELETE FROM blacklist
        WHERE blacklist.path = :path
    """
    )
    suspend fun delete(path: String)
}
