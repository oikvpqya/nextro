package me.oikvpqya.apps.music.feature

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object Main : Destination

    @Serializable
    data object Preference : Destination
}

sealed interface MainDestination {

    @Serializable
    data object Albums : MainDestination

    @Serializable
    data object Artists : MainDestination

    @Serializable
    data object Home : MainDestination

    @Serializable
    data object Playlists : MainDestination

    @Serializable
    data object Songs : MainDestination
}

sealed interface AlbumsDestination {

    @Serializable
    data object Albums : AlbumsDestination

    @Serializable
    data class Detail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : AlbumsDestination
}

sealed interface ArtistsDestination {

    @Serializable
    data object Artists : ArtistsDestination

    @Serializable
    data class Detail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : ArtistsDestination
}

sealed interface HomeDestination {

    @Serializable
    data object Home : HomeDestination

    @Serializable
    data class AlbumDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : HomeDestination

    @Serializable
    data class ArtistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : HomeDestination

    @Serializable
    data class PlaylistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : HomeDestination
}

sealed interface PlaylistsDestination {

    @Serializable
    data object Playlists : PlaylistsDestination

    @Serializable
    data class Detail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : PlaylistsDestination
}

sealed interface SongsDestination {

    @Serializable
    data object Songs : SongsDestination
}
