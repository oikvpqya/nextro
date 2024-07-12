package me.oikvpqya.apps.music.room.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.oikvpqya.apps.music.model.Tag

@Entity(
  tableName = "play_count",
)
data class PlayCountEntity(
  @PrimaryKey
  val mediaId: String,
  val count: Long,
  val tag: Tag.Song
)
