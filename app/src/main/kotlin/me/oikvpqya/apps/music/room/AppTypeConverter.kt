package me.oikvpqya.apps.music.room

import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.oikvpqya.apps.music.model.Tag

@ProvidedTypeConverter
class AppTypeConverter {

    @TypeConverter
    fun decodeSongTag(value: String?): Tag.Song? {
        Log.d("AppTypeConverter", "decodeSongTag")
        val tag: Tag.Song? = if (value != null) {
            Json.decodeFromString(string = value)
        } else null
        Log.d("AppTypeConverter", "_byteArray: ${value ?: "null"}, tag: ${tag ?: "null"}")
        return tag
    }

    @TypeConverter
    fun encodeSongTag(value: Tag.Song?): String? {
        val byteArray =  if (value != null) {
            Json.encodeToString(value = value)
                .also { Json.decodeFromString<Tag.Song>(string = it ).also { Log.d("AppTypeConverter", "tag: $it") } }
        } else null
        Log.d("AppTypeConverter", "byteArray: ${byteArray ?: "null"}, tag: ${value ?: "null"}")
        return byteArray
    }
}
