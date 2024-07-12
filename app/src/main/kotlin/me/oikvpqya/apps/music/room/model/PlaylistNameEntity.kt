package me.oikvpqya.apps.music.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
  tableName = "playlist_name",
)
data class PlaylistNameEntity(
  val name: String,
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L
)
