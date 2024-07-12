package me.oikvpqya.apps.music.mediastore

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.datetime.Clock
import me.oikvpqya.apps.music.data.MediaStoreRepository
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.mediastore.util.asSong
import me.oikvpqya.apps.music.model.Libraries
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.SortBy
import me.oikvpqya.apps.music.model.SortOrder
import me.oikvpqya.apps.music.model.asAlbumTag
import me.oikvpqya.apps.music.model.asArtistTag
import me.tatarka.inject.annotations.Inject

@Inject
class MediaStoreRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val preferenceRepository: PreferenceRepository,
) : MediaStoreRepository {

    init {
        Log.d("MediaStoreRepository", "init")
    }

    private val excludedFolders: List<String> = listOf("Whatsapp", "Audio")

    private val collection = if (Build.VERSION.SDK_INT >= 29) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    private val contentObserverSharedFlow = callbackFlow {
        val observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                trySend(Clock.System.now().toEpochMilliseconds())
            }
        }
        contentResolver.registerContentObserver(collection, true, observer)
        trySend(Clock.System.now().toEpochMilliseconds())
        awaitClose { contentResolver.unregisterContentObserver(observer) }
    }
        .shareIn(
            CoroutineScope(Dispatchers.Default),
            SharingStarted.Lazily,
            1
        )

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM_ARTIST,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.COMPOSER,
        MediaStore.Audio.Media.CD_TRACK_NUMBER,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DATE_MODIFIED,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.GENRE,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.YEAR
//        MediaStore.Audio.Media.ARTIST_ID,
//        MediaStore.Audio.Media.DATE_ADDED,
    )

    private val selection = buildString {
        append("${MediaStore.Audio.Media.IS_MUSIC} != 0")
        repeat(excludedFolders.size) {
            append(" AND ${MediaStore.Audio.Media.DATA} NOT LIKE ?")
        }
    }

    private val selectionArgs = excludedFolders.map { "%$it%" }.toTypedArray()

    override val songsFlow: Flow<List<Library.Song.Default>> = combine(
        preferenceRepository.songsSortByFlow,
        preferenceRepository.songsSortOrderFlow,
        contentObserverSharedFlow
    ) { sortBy, sortOrder, timestamp ->
        queryAsSongs(collection, projection, selection, selectionArgs, sortBy, sortOrder, timestamp)
    }

    override val albumsFlow: Flow<List<Libraries.Album>> = combine(
        preferenceRepository.albumsSortByFlow,
        preferenceRepository.albumsSortOrderFlow,
        contentObserverSharedFlow
    ) { sortBy, sortOrder, timestamp ->
        queryAsSongs(collection, projection, selection, selectionArgs, sortBy, sortOrder, timestamp)
            .let { songs ->
                if (songs.isNotEmpty()) {
                    songs.groupBy {
                        it.tag.albumId
                    }.map { (_, items) ->
                        Libraries.Album(
                            tag = items.first().tag.asAlbumTag(),
                            size = items.size
                        )
                    }
                } else emptyList()
            }
    }

    override val artistsFlow: Flow<List<Libraries.Default>> = combine(
        preferenceRepository.artistsSortByFlow,
        preferenceRepository.artistsSortOrderFlow,
        contentObserverSharedFlow
    ) { sortBy, sortOrder, timestamp ->
        queryAsSongs(collection, projection, selection, selectionArgs, sortBy, sortOrder, timestamp)
            .let { songs ->
                if (songs.isNotEmpty()) {
                    songs.groupBy {
                        it.tag.artist
                    }.map { (_, items) ->
                        Libraries.Default(
                            tag = items.first().tag.asArtistTag(),
                            size = items.size
                        )
                    }
                } else emptyList()
            }
    }

    override fun albumSongsFlow(albumId: Long): Flow<List<Library.Song.Default>> = combine(
        preferenceRepository.albumDetailSortByFlow,
        preferenceRepository.albumDetailSortOrderFlow,
        contentObserverSharedFlow
    ) { sortBy, sortOrder, _ ->
        queryAsAlbumSongs(collection, projection, selection, selectionArgs, sortBy, sortOrder, albumId)
    }

    override fun artistsSongsFlow(artist: String): Flow<List<Library.Song.Default>> = combine(
        preferenceRepository.artistDetailSortByFlow,
        preferenceRepository.artistDetailSortOrderFlow,
        contentObserverSharedFlow
    ) { sortBy, sortOrder, _ ->
        queryAsArtistSongs(collection, projection, selection, selectionArgs, sortBy, sortOrder, artist)
    }

    private fun queryAsAlbumSongs(
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortBy: SortBy,
        sortOrder: SortOrder,
        albumId: Long
    ): List<Library.Song.Default> {
        val albumIdSelection = "${MediaStore.Audio.Media.ALBUM_ID} = $albumId"
        return contentResolver.queryAsSongs(
            uri = uri,
            projection = projection,
            selection = "$albumIdSelection AND $selection",
            selectionArgs = selectionArgs,
            sortOrder = "$sortBy $sortOrder"
        )
    }

    private fun queryAsArtistSongs(
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortBy: SortBy,
        sortOrder: SortOrder,
        artist: String
    ): List<Library.Song.Default> {
        val artistSelection = "${MediaStore.Audio.Media.ARTIST} = '$artist'"
        return contentResolver.queryAsSongs(
            uri = uri,
            projection = projection,
            selection = "$artistSelection AND $selection",
            selectionArgs = selectionArgs,
            sortOrder = "$sortBy $sortOrder"
        )
    }

    private fun queryAsSongs(
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortBy: SortBy,
        sortOrder: SortOrder,
        timestamp: Long
    ): List<Library.Song.Default> {
        Log.d("MediaStoreRepository", "queryAsSongs: $timestamp")
        return contentResolver.queryAsSongs(
            uri = uri,
            projection = projection,
            selection = selection,
            selectionArgs = selectionArgs,
            sortOrder = "$sortBy $sortOrder"
        )
    }
}

fun ContentResolver.queryAsSongs(
    uri: Uri,
    projection: Array<String>,
    selection: String,
    selectionArgs: Array<String>,
    sortOrder: String
): List<Library.Song.Default> {
    return buildList {
        this@queryAsSongs.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                cursor
                    .asSong()
                    .let(::add)
            }
        }
    }
}
