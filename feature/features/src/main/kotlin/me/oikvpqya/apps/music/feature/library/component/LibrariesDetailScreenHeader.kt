package me.oikvpqya.apps.music.feature.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.oikvpqya.apps.music.ui.component.AppIcons
import me.oikvpqya.apps.music.ui.component.SummaryContainer
import me.oikvpqya.apps.music.ui.component.TitleContainer
import me.oikvpqya.apps.music.ui.component.fake.ImageContainerSample
import me.oikvpqya.apps.music.ui.media.getArtworkUri
import me.oikvpqya.apps.music.ui.util.LIST_TRACK_ALBUM_LARGE_NORMAL_SIZE

@Composable
fun LibrariesDetailScreenHeader(
    name: String,
    summary: String,
    albumId: Long,
    playText: String = "Play",
    shuffleText: String = "Shuffle"
) {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageContainerSample(data = getArtworkUri(albumId), size = LIST_TRACK_ALBUM_LARGE_NORMAL_SIZE)
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                TitleContainer(title = name)
                SummaryContainer(summary = summary)

                Row {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = AppIcons.More,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {},
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = AppIcons.Play,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(
                    modifier =  Modifier.size(ButtonDefaults.IconSpacing)
                )
                Text(
                    text = playText
                )
            }

            OutlinedButton(
                onClick = {},
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = AppIcons.Shuffle,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(
                    modifier =  Modifier.size(ButtonDefaults.IconSpacing)
                )
                Text(
                    text = shuffleText
                )
            }
        }
    }
}

//
//@Composable
//fun FirstItem() {
//    val thumbnail = object {
//        val uri = Uri.EMPTY
//        val size = 0.dp
//        val radius = 0.dp
//    }
//    data class Artist(
//        val id: String,
//        val name: String
//    )
//    val album = object {
//        val title = "title"
//        val year: Long? = 2024
//        val artists = listOf("A", "B")
//    }
//    val navController = rememberNavController()
//
//    Column(
//        modifier = Modifier.padding(12.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            AsyncImage(
//                model = thumbnail.uri,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(thumbnail.size)
//                    .clip(RoundedCornerShape(thumbnail.radius))
//            )
//
//            Spacer(Modifier.width(16.dp))
//
//            Column(
//                verticalArrangement = Arrangement.Center,
//            ) {
//                AutoResizeText(
//                    text = album.title,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    fontSizeRange = FontSizeRange(16.sp, 22.sp)
//                )
//
//                val annotatedString = buildAnnotatedString {
//                    withStyle(
//                        style = MaterialTheme.typography.titleMedium.copy(
//                            fontWeight = FontWeight.Normal,
//                            color = MaterialTheme.colorScheme.onBackground
//                        ).toSpanStyle()
//                    ) {
//                        album.artists.distinct().fastForEachIndexed { index, artist ->
//                            pushStringAnnotation(artist, artist)
//                            append(artist)
//                            pop()
//                            if (index != album.artists.lastIndex) {
//                                append(", ")
//                            }
//                        }
//                    }
//                }
//                ClickableText(annotatedString) { offset ->
//                    annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.let { range ->
//                        navController.navigate("artist/${range.tag}")
//                    }
//                }
//
//                if (album.year != null) {
//                    Text(
//                        text = album.year.toString(),
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Normal
//                    )
//                }
//
//                Row {
//
//                    // Bookmark Button
////                    IconButton(
////                        onClick = {
////                            database.query {
////                                update(albumWithSongs.album.toggleLike())
////                            }
////                        }
////                    ) {
////                        Icon(
////                            painter = painterResource(if (albumWithSongs.album.bookmarkedAt != null) R.drawable.favorite else R.drawable.favorite_border),
////                            contentDescription = null,
////                            tint = if (albumWithSongs.album.bookmarkedAt != null) MaterialTheme.colorScheme.error else LocalContentColor.current
////                        )
////                    }
//
//                    // Download Button
////                    when (downloadState) {
////                        Download.STATE_COMPLETED -> {
////                            IconButton(
////                                onClick = {
////                                    albumWithSongs.songs.forEach { song ->
////                                        DownloadService.sendRemoveDownload(
////                                            context,
////                                            ExoDownloadService::class.java,
////                                            song.id,
////                                            false
////                                        )
////                                    }
////                                }
////                            ) {
////                                Icon(
////                                    painter = painterResource(R.drawable.offline),
////                                    contentDescription = null
////                                )
////                            }
////                        }
////
////                        Download.STATE_DOWNLOADING -> {
////                            IconButton(
////                                onClick = {
////                                    albumWithSongs.songs.forEach { song ->
////                                        DownloadService.sendRemoveDownload(
////                                            context,
////                                            ExoDownloadService::class.java,
////                                            song.id,
////                                            false
////                                        )
////                                    }
////                                }
////                            ) {
////                                CircularProgressIndicator(
////                                    strokeWidth = 2.dp,
////                                    modifier = Modifier.size(24.dp)
////                                )
////                            }
////                        }
////
////                        else -> {
////                            IconButton(
////                                onClick = {
////                                    albumWithSongs.songs.forEach { song ->
////                                        val downloadRequest = DownloadRequest.Builder(song.id, song.id.toUri())
////                                            .setCustomCacheKey(song.id)
////                                            .setData(song.song.title.toByteArray())
////                                            .build()
////                                        DownloadService.sendAddDownload(
////                                            context,
////                                            ExoDownloadService::class.java,
////                                            downloadRequest,
////                                            false
////                                        )
////                                    }
////                                }
////                            ) {
////                                Icon(
////                                    painter = painterResource(R.drawable.download),
////                                    contentDescription = null
////                                )
////                            }
////                        }
////                    }
//
//                    //More Button
////                    IconButton(
////                        onClick = {
////                            menuState.show {
////                                AlbumMenu(
////                                    originalAlbum = Album(albumWithSongs.album, albumWithSongs.artists),
////                                    navController = navController,
////                                    onDismiss = menuState::dismiss
////                                )
////                            }
////                        }
////                    ) {
////                        Icon(
////                            painter = painterResource(R.drawable.more_vert),
////                            contentDescription = null
////                        )
////                    }
//                }
//            }
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//            Button(
//                onClick = {
////                    playerConnection.playQueue(
////                        ListQueue(
////                            title = albumWithSongs.album.title,
////                            items = albumWithSongs.songs.map(Song::toMediaItem)
////                        )
////                    )
//                },
//                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
//                modifier = Modifier.weight(1f)
//            ) {
////                Icon(
////                    painter = painterResource(R.drawable.play),
////                    contentDescription = null,
////                    modifier = Modifier.size(ButtonDefaults.IconSize)
////                )
////                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
////                Text(
////                    text = stringResource(R.string.play)
////                )
//            }
//
//            OutlinedButton(
//                onClick = {
////                    playerConnection.playQueue(
////                        ListQueue(
////                            title = albumWithSongs.album.title,
////                            items = albumWithSongs.songs.shuffled().map(Song::toMediaItem)
////                        )
////                    )
//                },
//                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
//                modifier = Modifier.weight(1f)
//            ) {
////                Icon(
////                    painter = painterResource(R.drawable.shuffle),
////                    contentDescription = null,
////                    modifier = Modifier.size(ButtonDefaults.IconSize)
////                )
////                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
////                Text(stringResource(R.string.shuffle))
//            }
//        }
//    }
//}
