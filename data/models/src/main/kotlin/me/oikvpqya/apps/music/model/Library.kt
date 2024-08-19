package me.oikvpqya.apps.music.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Library {
    val tag: Tag
    val name: String
    val summary: String

    sealed interface Song : Library {
        override val tag: Tag.Song

        data class Default(
            private val mediaId: Long,
            private val album: String,
            private val artist: String,
            private val title: String,
            private val albumArtist: String,
            private val composer: String,
            private val data: String,
            private val dateModified: Long,
            private val duration: Long,
            private val genre: String,
            private val trackNumber: Long,
            private val year: Long,
            private val albumId: Long,
        ) : Song {

            constructor(tag: Tag.Song) : this(
                tag.mediaId, tag.album, tag.artist, tag.title, tag.albumArtist, tag.composer,
                tag.data, tag.dateModified, tag.duration, tag.genre, tag.trackNumber, tag.year,
                tag.albumId,
            )

            override val tag: Tag.Song
                get() = Tag.Song(
                    mediaId, album, artist, title, albumArtist, composer, data, dateModified,
                    duration, genre, trackNumber, year, albumId,
                )
            override val name: String
                get() = title
            override val summary: String
                get() = artist

        }

        data class History(
            private val mediaId: Long,
            private val album: String,
            private val artist: String,
            private val title: String,
            private val albumArtist: String,
            private val composer: String,
            private val data: String,
            private val dateModified: Long,
            private val duration: Long,
            private val genre: String,
            private val trackNumber: Long,
            private val year: Long,
            private val albumId: Long,
            private val timestamp: Long,
        ) : Song {

            override val tag: Tag.Song
                get() = Tag.Song(
                    mediaId, album, artist, title, albumArtist, composer, data, dateModified,
                    duration, genre, trackNumber, year, albumId,
                )
            override val name: String
                get() = title
            override val summary: String
                get() = artist

            constructor(tag: Tag.Song, timestamp: Long) : this(
                tag.mediaId, tag.album, tag.artist, tag.title, tag.albumArtist, tag.composer,
                tag.data, tag.dateModified, tag.duration, tag.genre, tag.trackNumber, tag.year,
                tag.albumId, timestamp
            )
        }

        data class PlayCount(
            private val mediaId: Long,
            private val album: String,
            private val artist: String,
            private val title: String,
            private val albumArtist: String,
            private val composer: String,
            private val data: String,
            private val dateModified: Long,
            private val duration: Long,
            private val genre: String,
            private val trackNumber: Long,
            private val year: Long,
            private val albumId: Long,
            private val count: Long,
        ) : Song {

            override val tag: Tag.Song
                get() = Tag.Song(
                    mediaId, album, artist, title, albumArtist, composer, data, dateModified,
                    duration, genre, trackNumber, year, albumId,
                )
            override val name: String
                get() = title
            override val summary: String
                get() = artist

            constructor(tag: Tag.Song, count: Long) : this(
                tag.mediaId, tag.album, tag.artist, tag.title, tag.albumArtist, tag.composer,
                tag.data, tag.dateModified, tag.duration, tag.genre, tag.trackNumber, tag.year,
                tag.albumId, count
            )
        }
    }
}

@Serializable
sealed interface Libraries: Library {
//    val items: List<Library.Song>
    val size: Int

    @Serializable
    data class Album(
        val albumArtist: String,
        val genre: String,
        val year: Long,
        val albumId: Long,
        override val name: String,
        override val size: Int,
    ) : Libraries {
        override val summary: String
            get() = albumArtist
        override val tag: Tag
            get() = Tag.Album(albumArtist, genre, name, year, albumId)
        constructor(tag: Tag.Album, size: Int) : this(
            tag.albumArtist, tag.genre, tag.year, tag.albumId, tag.name, size
        )
    }

    @Serializable
    data class Playlist(
        val albumId: Long,
        override val name: String,
        override val size: Int,
    ) : Libraries {
        override val tag: Tag.Default
            get() = Tag.Default(
                name = name,
                albumId = albumId,
            )
        override val summary: String
            get() = "$size songs"
        constructor(tag: Tag.Default, size: Int) : this(tag.albumId, tag.name, size)
    }

    @Serializable
    data class Default(
        val albumId: Long,
        override val name: String,
        override val size: Int,
    ) : Libraries {
        override val tag: Tag.Default
            get() = Tag.Default(
                name = name,
                albumId = albumId,
            )
        override val summary: String
            get() = "$size songs"
        constructor(tag: Tag.Default, size: Int) : this(tag.albumId, tag.name, size)
    }
}
