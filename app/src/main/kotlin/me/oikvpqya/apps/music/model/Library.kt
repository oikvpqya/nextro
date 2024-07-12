package me.oikvpqya.apps.music.model

import androidx.room.Ignore
import kotlinx.serialization.Serializable

@Serializable
sealed interface Library {
    val tag: Tag
    val name: String
    val summary: String

    sealed interface Song : Library {
        override val tag: Tag.Song

        data class Default(
            override val tag: Tag.Song,
            @Ignore
            override val name: String,
            @Ignore
            override val summary: String
        ) : Song {
            constructor(tag: Tag.Song) : this(tag, tag.title, tag.artist)
        }

        data class History(
            override val tag: Tag.Song,
            val timestamp: Long,
            @Ignore
            override val name: String,
            @Ignore
            override val summary: String
        ) : Song {
            constructor(tag: Tag.Song, timestamp: Long) : this(tag, timestamp, tag.title, tag.artist)
        }

        data class PlayCount(
            override val tag: Tag.Song,
            val count: Long,
            @Ignore
            override val name: String,
            @Ignore
            override val summary: String
        ) : Song {
            constructor(tag: Tag.Song, count: Long) : this(tag, count, tag.title, tag.artist)
        }
    }
}

@Serializable
sealed interface Libraries: Library {
//    val items: List<Library.Song>
    val size: Int

    @Serializable
    data class Album(
        override val tag: Tag.Album,
        override val size: Int,
        override val name: String = tag.name,
        override val summary: String = tag.albumArtist
    ) : Libraries

    @Serializable
    data class Playlist(
        override val tag: Tag.Default,
        override val size: Int,
        override val name: String = tag.name,
        override val summary: String = "$size songs",
    ) : Libraries

    @Serializable
    data class Default(
        override val tag: Tag.Default,
        override val size: Int,
        override val name: String = tag.name,
        override val summary: String = "$size songs",
    ) : Libraries
}
