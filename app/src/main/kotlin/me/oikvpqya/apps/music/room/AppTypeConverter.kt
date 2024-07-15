package me.oikvpqya.apps.music.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.oikvpqya.apps.music.model.Tag

@ProvidedTypeConverter
class AppTypeConverter {

    @TypeConverter
    fun decodeSongTag(value: String?): Tag.Song? {
        val tag: Tag.Song? = if (value != null) { Json.decodeFromString(string = value) } else null
        return tag
    }

    @TypeConverter
    fun encodeSongTag(value: Tag.Song?): String? {
        val byteArray =  if (value != null) { Json.encodeToString(value = value) } else null
        return byteArray
    }
}
