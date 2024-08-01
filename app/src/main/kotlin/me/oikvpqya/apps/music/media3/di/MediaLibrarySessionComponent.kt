package me.oikvpqya.apps.music.media3.di

import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.media3.AppMediaLibraryService
import me.oikvpqya.apps.music.media3.util.asInt
import me.oikvpqya.apps.music.media3.util.asMediaItem
import me.oikvpqya.apps.music.media3.util.asPlaybackRepeatMode
import me.tatarka.inject.annotations.Provides

interface MediaLibrarySessionComponent {

    val player: Player
    val session: MediaLibraryService.MediaLibrarySession

    @OptIn(UnstableApi::class)
    @Provides
    @MediaLibraryServiceScope
    fun provideRenderersFactory(
        service: AppMediaLibraryService,
    ): RenderersFactory {
        return RenderersFactory { eventHandler, _, audioRendererEventListener, _, _ ->
            arrayOf(
                MediaCodecAudioRenderer(
                    service,
                    MediaCodecSelector.DEFAULT,
                    eventHandler,
                    audioRendererEventListener,
                ),
            )
        }
    }

    @OptIn(UnstableApi::class)
    @Provides
    @MediaLibraryServiceScope
    fun providePlayer(
        service: AppMediaLibraryService,
        coroutineScope: MediaLibraryServiceCoroutineScope,
        databaseRepository: AppDatabaseRepository,
        preferenceRepository: PreferenceRepository,
        renderersFactory: RenderersFactory,
    ) : Player {
        return ExoPlayer.Builder(service)
            .apply {
                val attributes = AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build()
                setAudioAttributes(attributes, true)
            }
            .setRenderersFactory(renderersFactory)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
                repeatMode = runBlocking { preferenceRepository.repeatModeFlow.first().asInt() }
                shuffleModeEnabled = runBlocking { preferenceRepository.shuffleModeFlow.first() }
                addListener(
                    object: Player.Listener {
                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            val currentMediaItemIndex = this@apply.currentMediaItemIndex
                            coroutineScope.launch {
                                preferenceRepository.updateQueueIndex(currentMediaItemIndex)
                                if (mediaItem != null) {
                                    databaseRepository.setHistory(listOf(mediaItem))
                                }
                            }
                        }

                        override fun onRepeatModeChanged(repeatMode: Int) {
                            coroutineScope.launch {
                                val mode = repeatMode.asPlaybackRepeatMode()
                                preferenceRepository.updateRepeatMode(mode)
                            }
                        }

                        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                            coroutineScope.launch {
                                preferenceRepository.updateShuffleMode(shuffleModeEnabled)
                            }
                        }
                    }
                )
            }
    }

    @Provides
    @MediaLibraryServiceScope
    fun provideMediaLibrarySessionCallback(
        coroutineScope: MediaLibraryServiceCoroutineScope,
        databaseRepository: AppDatabaseRepository,
        preferenceRepository: PreferenceRepository,
    ): MediaLibraryService.MediaLibrarySession.Callback {
        return object : MediaLibraryService.MediaLibrarySession.Callback {
            override fun onAddMediaItems(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo,
                mediaItems: MutableList<MediaItem>
            ): ListenableFuture<MutableList<MediaItem>> {
                return coroutineScope.future {
                    databaseRepository.setQueues(mediaItems)
                    mediaItems
                }
            }

            @OptIn(UnstableApi::class)
            override fun onPlaybackResumption(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo,
            ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
                return coroutineScope.future {
                    combine(
                        databaseRepository.queueFlow,
                        preferenceRepository.queueIndexFlow,
                    ) { queue, index ->
                        val mediaItems = queue.map { it.tag.asMediaItem() }
                        MediaSession.MediaItemsWithStartPosition(mediaItems, index, 0L)
                    }.first()
                }
            }
        }
    }

    @Provides
    @MediaLibraryServiceScope
    fun provideMediaLibrarySession(
        service: AppMediaLibraryService,
        player: Player,
        sessionCallback: MediaLibraryService.MediaLibrarySession.Callback,
    ):  MediaLibraryService.MediaLibrarySession {
        return MediaLibraryService.MediaLibrarySession.Builder(service, player, sessionCallback)
            .build()
    }
}
