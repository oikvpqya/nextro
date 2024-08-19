package me.oikvpqya.apps.music.mediastore

import me.oikvpqya.apps.music.data.ArtworkProvider
import me.oikvpqya.apps.music.mediastore.util.getArtworkUri
import me.tatarka.inject.annotations.Inject

@Inject
class ArtworkProviderImpl : ArtworkProvider {
    override fun getArtwork(albumId: Long): Any  {
        return getArtworkUri(albumId)
    }
}
