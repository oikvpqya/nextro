package me.oikvpqya.apps.music.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Tag {
    val albumId: Long

    @Serializable
    data class Song(
        val mediaId: Long,
        val album: String,
        val artist: String,
        val title: String,
        val albumArtist: String,
        val composer: String,
        val data: String,
        val dateModified: Long,
        val duration: Long,
        val genre: String,
        val trackNumber: Long,
        val year: Long,
        override val albumId: Long
    ) : Tag {
        companion object {
            val Empty = Song(
                album = "",
                albumArtist = "",
                albumId = -1L,
                artist = "",
                title = "",
                composer = "",
                data = "",
                dateModified = -1L,
                duration = -1L,
                genre = "",
                mediaId = -1L,
                trackNumber = -1L,
                year = -1L
            )
        }
    }

    @Serializable
    data class Album(
        val albumArtist: String,
        val genre: String,
        val name: String,
        val year: Long,
        override val albumId: Long
    ) : Tag {
        companion object {
            val Empty = Album(
                albumArtist = "",
                genre = "",
                year = -1L,
                albumId = -1L,
                name = ""
            )
        }
    }

    @Serializable
    data class Default(
        val name: String,
        override val albumId: Long
    ) : Tag {
        companion object {
            val Empty = Default(
                albumId = -1L,
                name = ""
            )
        }
    }
}

fun Tag.Song.asAlbumTag(): Tag.Album {
    return Tag.Album(
        albumArtist = albumArtist.ifBlank { artist },
        genre = genre,
        year = year,
        albumId = albumId,
        name = album
    )
}

fun Tag.Song.asArtistTag(): Tag.Default {
    return Tag.Default(
        albumId = albumId,
        name = artist
    )
}

fun Tag.Song.asPlaylistTag(name: String): Tag.Default {
    return Tag.Default(
        albumId = albumId,
        name = name
    )
}
