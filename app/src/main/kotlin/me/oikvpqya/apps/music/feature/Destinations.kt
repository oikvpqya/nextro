package me.oikvpqya.apps.music.feature

import kotlinx.serialization.Serializable

interface Destination

sealed interface AppDestination : Destination {

    @Serializable
    data object Main : AppDestination

    @Serializable
    data object Preference : AppDestination
}

sealed interface MainDestination : Destination {

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

sealed interface PreferenceDestination : Destination {

    @Serializable
    data object Root : PreferenceDestination
}

val MainDestination.startDestination: Destination
    get() = when (this) {
        MainDestination.Albums -> AlbumsDestination.Albums
        MainDestination.Artists -> ArtistsDestination.Artists
        MainDestination.Home -> HomeDestination.Home
        MainDestination.Playlists -> PlaylistsDestination.Playlists
        MainDestination.Songs -> SongsDestination.Songs
    }

val AppDestination.startDestination: Destination
    get() = when (this) {
        AppDestination.Main -> MainDestination.Home
        AppDestination.Preference -> PreferenceDestination.Root
    }

sealed interface AlbumsDestination : Destination {

    @Serializable
    data object Albums : AlbumsDestination

    @Serializable
    data class AlbumDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : AlbumsDestination

    @Serializable
    data class ArtistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : AlbumsDestination

    @Serializable
    data class PlaylistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : AlbumsDestination
}

sealed interface ArtistsDestination : Destination {

    @Serializable
    data object Artists : ArtistsDestination

    @Serializable
    data class AlbumDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : ArtistsDestination

    @Serializable
    data class ArtistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : ArtistsDestination

    @Serializable
    data class PlaylistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : ArtistsDestination
}

sealed interface HomeDestination : Destination {

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

sealed interface PlaylistsDestination : Destination {

    @Serializable
    data object Playlists : PlaylistsDestination

    @Serializable
    data class AlbumDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : PlaylistsDestination

    @Serializable
    data class ArtistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : PlaylistsDestination

    @Serializable
    data class PlaylistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : PlaylistsDestination
}

sealed interface SongsDestination : Destination {

    @Serializable
    data object Songs : SongsDestination

    @Serializable
    data class AlbumDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : SongsDestination

    @Serializable
    data class ArtistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : SongsDestination

    @Serializable
    data class PlaylistDetail(
        val name: String,
        val summary: String,
        val albumId: Long,
    ) : SongsDestination
}
