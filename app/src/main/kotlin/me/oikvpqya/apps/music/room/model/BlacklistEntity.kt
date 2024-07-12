package me.oikvpqya.apps.music.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
  tableName = "blacklist",
)
data class BlacklistEntity(
  @PrimaryKey
  val path: String
)
