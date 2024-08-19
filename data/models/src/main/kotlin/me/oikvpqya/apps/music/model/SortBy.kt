package me.oikvpqya.apps.music.model

import android.provider.MediaStore

enum class SortBy {
    TITLE,
    ARTIST,
    ALBUM,
    DURATION,
    DATE_ADDED
    ;

    override fun toString(): String {
        return when (this) {
            TITLE -> MediaStore.Audio.Media.TITLE
            ARTIST -> MediaStore.Audio.Media.ARTIST
            ALBUM -> MediaStore.Audio.Media.ALBUM
            DURATION -> MediaStore.Audio.Media.DURATION
            DATE_ADDED -> MediaStore.Audio.Media.DATE_ADDED
        }
    }
}
