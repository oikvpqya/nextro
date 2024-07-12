package me.oikvpqya.apps.music.media3

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.guava.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.oikvpqya.apps.music.app.MainActivity
import me.oikvpqya.apps.music.app.di.applicationComponent
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.media3.di.AppMediaLibraryServiceComponent
import me.oikvpqya.apps.music.media3.util.asInt
import me.oikvpqya.apps.music.media3.util.asMediaItem
import me.oikvpqya.apps.music.media3.util.asPlaybackRepeatMode
import me.oikvpqya.apps.music.model.Library

class AppMediaLibraryService : MediaLibraryService() {

    private val databaseRepository: AppDatabaseRepository by lazy { applicationComponent.appDatabaseRepository }
    private val preferenceRepository: PreferenceRepository by lazy { applicationComponent.preferenceRepository }

    private val defaultCoroutineScope by lazy { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    private val player by lazy {
        ExoPlayer.Builder(this)
            .apply {
                val attributes = AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build()
                setAudioAttributes(attributes, true)
            }
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
                repeatMode = runBlocking { preferenceRepository.repeatModeFlow.first().asInt() }
                shuffleModeEnabled = runBlocking { preferenceRepository.shuffleModeFlow.first() }
                addListener(
                    object: Player.Listener {
                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            val currentMediaItemIndex = this@apply.currentMediaItemIndex
                            defaultCoroutineScope.launch {
                                preferenceRepository.updateQueueIndex(currentMediaItemIndex)
                                if (mediaItem != null) {
                                    databaseRepository.setHistory(listOf(mediaItem))
                                }
                            }
                        }

                        override fun onRepeatModeChanged(repeatMode: Int) {
                            defaultCoroutineScope.launch {
                                preferenceRepository.updateRepeatMode(repeatMode.asPlaybackRepeatMode())
                            }
                        }

                        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                            defaultCoroutineScope.launch {
                                Log.d("shuffleModeEnabled", "shuffleModeEnabled: $shuffleModeEnabled")
                                preferenceRepository.updateShuffleMode(shuffleModeEnabled)
                            }
                        }
                    }
                )
            }
    }

    private val sessionCallback by lazy {
        object : MediaLibrarySession.Callback {
            override fun onAddMediaItems(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo,
                mediaItems: MutableList<MediaItem>
            ): ListenableFuture<MutableList<MediaItem>> {
                return defaultCoroutineScope.future {
                    databaseRepository.setQueues(mediaItems)
                    mediaItems
                }
            }

            @OptIn(UnstableApi::class)
            override fun onPlaybackResumption(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo
            ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
                return defaultCoroutineScope.future {
                    val f: Flow<List<Library.Song.Default>> = flow { emit(listOf()) }
                    combine(
                        databaseRepository.queueFlow,
//                        f,
                        preferenceRepository.queueIndexFlow
                    ) { queue, index ->
                        MediaSession.MediaItemsWithStartPosition(queue.map { it.tag.asMediaItem() }, index, 0L)
                    }.first()
                }
            }
        }
    }

    private val session by lazy {
        MediaLibrarySession.Builder(this, player, sessionCallback)
            .apply {
                val pendingIntent = PendingIntent.getActivity(
                    this@AppMediaLibraryService,
                    0,
                    Intent(this@AppMediaLibraryService, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
                if (pendingIntent != null) {
                    setSessionActivity(pendingIntent)
                }
            }
            .build()
    }

    private var isReleased = false

    override fun onCreate() {
        Log.d("AppMediaLibraryService", "onCreate")
        super.onCreate()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        Log.d("AppMediaLibraryService", "onGetSession")
        return session.takeUnless { session ->
            session.invokeIsReleased
        }.also {
            if (it == null) {
                Log.e("AppMediaLibraryService", "onGetSession: returns null because the session is already released")
            }
        }
    }

    override fun onDestroy() {
        Log.d("AppMediaLibraryService", "onDestroy")
        if (!isReleased) {
            session.release()
            player.release()
        }
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("AppMediaLibraryService", "onTaskRemoved")
        isReleased = true
        session.release()
        player.release()
        stopSelf()
    }
}

private val MediaSession.invokeIsReleased: Boolean
    get() = try {
        // temporarily checked to debug
        // https://github.com/androidx/media/issues/422
        MediaSession::class.java.getDeclaredMethod("isReleased")
            .apply { isAccessible = true }
            .invoke(this) as Boolean
    } catch (e: Exception) {
        false
    }
