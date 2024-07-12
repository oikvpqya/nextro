package me.oikvpqya.apps.music.mediastore.util

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.Tag

//internal fun String.asFilename(): String = File(this).name
//internal fun String.asFolder(): String = File(this).parentFile?.path.orEmpty()

internal fun Cursor.asSong(): Library.Song.Default {
    return Library.Song.Default(
        tag = Tag.Song(
            mediaId = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media._ID)),
            album = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)),
            albumArtist = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)) ?: "",
            albumId = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)),
            artist = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) ?: "",
            composer = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)) ?: "",
            data = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) ?: "",
            dateModified = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)),
            duration = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)),
            genre = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)) ?: "",
            title = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)) ?: "",
            trackNumber = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.CD_TRACK_NUMBER)),
            year = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))
        )
    )
}

fun Tag.getArtworkUri(): Uri {
    return ContentUris.withAppendedId(
        Uri.parse(AppMediaStoreConstant.MEDIA_STORE_ALBUM_ART_URI),
        albumId
    )
}

fun getArtworkUri(albumId: Long): Uri {
    return ContentUris.withAppendedId(
        Uri.parse(AppMediaStoreConstant.MEDIA_STORE_ALBUM_ART_URI),
        albumId
    )
}

fun Library.getArtworkUri(): Uri {
    return getArtworkUri(tag.albumId)
}

fun Library.Song.getMediaUri(): Uri {
    return ContentUris.withAppendedId(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        tag.mediaId
    )
}

fun Tag.Song.getMediaUri(): Uri {
    return ContentUris.withAppendedId(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        mediaId
    )
}
