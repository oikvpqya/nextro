package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistNameDao {

    @Entity(
        tableName = "playlist_name",
    )
    data class PlaylistNameEntity(
        val name: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
    )

    @Query(
        value = """
        SELECT playlist_name.name FROM playlist_name
    """
    )
    fun getAll(): Flow<List<String>>

    @Query(
        value = """
        INSERT INTO playlist_name (name)
        VALUES (:name)
    """
    )
    suspend fun insert(name: String)

    @Query(
        value = """
        DELETE FROM playlist_name
        WHERE playlist_name.name = :name
    """
    )
    suspend fun delete(name: String)

    @Query(
        value = """
        DELETE FROM playlist_name
    """
    )
    suspend fun deleteAll()
}
