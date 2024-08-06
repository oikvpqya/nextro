package me.oikvpqya.apps.music.room.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.oikvpqya.apps.music.model.Library

@Dao
interface PlaylistSongDao {

    @Entity(
        tableName = "playlist_song",
    )
    data class PlaylistSongEntity(
        val playlistId: Long,
        val mediaId: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
    )

    @Query(
        value = """
        SELECT song.* FROM song, playlist_song
        WHERE playlist_song.playlistId = :playlistId
        AND song.mediaId = playlist_song.mediaId
    """
    )
    fun getAll(playlistId: Long): Flow<List<Library.Song.Default>>

//    @Query(
//        value = """
//        SELECT * FROM playlist_song
//    """
//    )
//    fun getItemEntities(): Flow<List<PlaylistSongEntity>>

    @Query(
        value = """
        INSERT INTO playlist_song
        VALUES (:id, :playlistId, :mediaId)
    """
    )
    suspend fun insert(playlistId: Long, mediaId: String, id: Long = 0L)

    @Query(
        value = """
        DELETE FROM playlist_song
        WHERE playlist_song.playlistId = :playlistId
        AND playlist_song.mediaId = :mediaId
    """
    )
    suspend fun delete(playlistId: Long, mediaId: String)

    @Query(
        value = """
        DELETE FROM playlist_song
        WHERE playlist_song.playlistId = :playlistId
    """
    )
    suspend fun deleteAll(playlistId: Long)
}
