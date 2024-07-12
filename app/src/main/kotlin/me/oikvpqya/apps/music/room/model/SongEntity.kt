package me.oikvpqya.apps.music.room.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.oikvpqya.apps.music.model.Tag

@Entity(
  tableName = "song",
)
data class SongEntity(
  @PrimaryKey
  val mediaId: Long,
  val tag: Tag.Song
)
