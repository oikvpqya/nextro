package me.oikvpqya.apps.music.media3.util

import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.oikvpqya.apps.music.mediastore.util.getMediaUri
import me.oikvpqya.apps.music.model.PlaybackRepeatMode
import me.oikvpqya.apps.music.model.PlaybackState
import me.oikvpqya.apps.music.model.Tag

fun Int.asPlaybackState() = when (this) {
    Player.STATE_IDLE -> PlaybackState.IDLE
    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
    Player.STATE_READY -> PlaybackState.READY
    Player.STATE_ENDED -> PlaybackState.ENDED
    else -> error("Invalid playback state.")
}

fun Int.asPlaybackRepeatMode() = when (this) {
    Player.REPEAT_MODE_OFF -> PlaybackRepeatMode.OFF
    Player.REPEAT_MODE_ONE -> PlaybackRepeatMode.ONE
    Player.REPEAT_MODE_ALL -> PlaybackRepeatMode.ALL
    else -> error("Invalid playback repeat mode.")
}

fun PlaybackRepeatMode.asInt() = when (this) {
    PlaybackRepeatMode.OFF -> Player.REPEAT_MODE_OFF
    PlaybackRepeatMode.ONE -> Player.REPEAT_MODE_ONE
    PlaybackRepeatMode.ALL -> Player.REPEAT_MODE_ALL
}

fun Tag.Song.asMediaItem(): MediaItem {
    val mediaUri = getMediaUri()
    return MediaItem.Builder()
        .setMediaId(mediaId.toString())
        .setRequestMetadata(
            MediaItem.RequestMetadata.Builder()
                .setMediaUri(mediaUri)
                .build()
        )
        .setUri(mediaUri)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setArtworkUri(getArtworkUri())
                .setAlbumTitle(album)
                .setArtist(artist)
                .setTitle(title)
                .setIsBrowsable(false)
                .setIsPlayable(true)
                .setExtras(
                    bundleOf(
                        AppMediaConstant.MEDIA_ITEM_ALBUM_ID to albumId,
                        AppMediaConstant.MEDIA_ITEM_COMPOSER to composer,
                        AppMediaConstant.MEDIA_ITEM_DATA to data,
                        AppMediaConstant.MEDIA_ITEM_DATE_MODIFIED to dateModified,
                        AppMediaConstant.MEDIA_ITEM_DURATION to duration,
                        AppMediaConstant.MEDIA_ITEM_GENRE to genre,
                        AppMediaConstant.MEDIA_ITEM_TRACK_NUMBER to trackNumber,
                        AppMediaConstant.MEDIA_ITEM_YEAR to year,
                    )
                )
                .build()
        )
        .build()
}

fun MediaItem.asTag(): Tag.Song {
    return with(this.mediaMetadata) {
        Tag.Song(
            mediaId = mediaId.toLongOrNull() ?: return Tag.Song.Empty,
            album = albumTitle?.toString() ?: "",
            artist = artist?.toString() ?: "",
            title = title?.toString() ?: "",
            albumArtist = extras?.getString(AppMediaConstant.MEDIA_ITEM_ALBUM_ARTIST) ?: "",
            albumId = extras?.getLong(AppMediaConstant.MEDIA_ITEM_ALBUM_ID) ?: -1L,
            composer = extras?.getString(AppMediaConstant.MEDIA_ITEM_COMPOSER) ?: "",
            data = extras?.getString(AppMediaConstant.MEDIA_ITEM_DATA) ?: "",
            dateModified = extras?.getLong(AppMediaConstant.MEDIA_ITEM_DATE_MODIFIED) ?: -1L,
            duration = extras?.getLong(AppMediaConstant.MEDIA_ITEM_DURATION) ?: -1L,
            genre = extras?.getString(AppMediaConstant.MEDIA_ITEM_GENRE) ?: "",
            trackNumber = extras?.getLong(AppMediaConstant.MEDIA_ITEM_TRACK_NUMBER) ?: -1L,
            year = extras?.getLong(AppMediaConstant.MEDIA_ITEM_YEAR) ?: -1L

        )
    }
}


