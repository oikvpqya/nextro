package me.oikvpqya.apps.music.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.oikvpqya.apps.music.model.Tag

@Entity(
  tableName = "history",
)
data class HistoryEntity(
  val timestamp: Long,
  val mediaId: String,
  val tag: Tag.Song,
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L
)
