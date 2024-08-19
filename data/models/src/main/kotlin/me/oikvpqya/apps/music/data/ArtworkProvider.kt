package me.oikvpqya.apps.music.data

interface ArtworkProvider {
    fun getArtwork(albumId: Long): Any
}
