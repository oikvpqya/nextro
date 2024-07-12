package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag
import me.oikvpqya.apps.music.room.model.PlaylistNameEntity

@Dao
interface PlaylistDao {
    @Query(
        value = """
        SELECT * FROM playlist_name
    """
    )
    fun getAllNames(): Flow<List<PlaylistNameEntity>>

    @Query(
        value = """
        INSERT INTO playlist_name (name)
        VALUES (:name)
    """
    )
    suspend fun insertName(name: String)

    @Query(
        value = """
        DELETE FROM playlist_name
        WHERE name = :name
    """
    )
    suspend fun deleteName(name: String)

    @Query(
        value = """
        DELETE FROM playlist_name
    """
    )
    suspend fun deleteAllNames()

    @Query(
        value = """
        SELECT tag FROM playlist_song
        WHERE playlistId = :playlistId
    """
    )
    fun getAllItems(playlistId: Long): Flow<List<Library.Song.Default>>

//    @Query(
//        value = """
//        SELECT * FROM playlist_song
//    """
//    )
//    fun getItemEntities(): Flow<List<PlaylistSongEntity>>

    @Query(
        value = """
        INSERT INTO playlist_song
        VALUES (:id, :playlistId, :mediaId, :tag)
    """
    )
    suspend fun insertItem(playlistId: Long, mediaId: String, tag: Tag.Song, id: Long = 0L)

    @Query(
        value = """
        DELETE FROM playlist_song
        WHERE playlistId = :playlistId AND mediaId = :mediaId
    """
    )
    suspend fun deleteItem(playlistId: Long, mediaId: String)

    @Query(
        value = """
        DELETE FROM playlist_song
        WHERE playlistId = :playlistId
    """
    )
    suspend fun deleteAllItems(playlistId: Long)
}
