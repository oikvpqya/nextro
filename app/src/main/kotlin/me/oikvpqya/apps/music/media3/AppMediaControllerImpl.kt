package me.oikvpqya.apps.music.media3

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.launch
import me.oikvpqya.apps.music.data.AppDatabaseRepository
import me.oikvpqya.apps.music.data.AppMediaController
import me.oikvpqya.apps.music.data.AppMediaHandler
import me.oikvpqya.apps.music.data.AppMediaInfo
import me.oikvpqya.apps.music.data.PreferenceRepository
import me.oikvpqya.apps.music.media3.util.asInt
import me.oikvpqya.apps.music.media3.util.asMediaItem
import me.oikvpqya.apps.music.media3.util.asPlaybackRepeatMode
import me.oikvpqya.apps.music.media3.util.asPlaybackState
import me.oikvpqya.apps.music.media3.util.asTag
import me.oikvpqya.apps.music.model.Library
import me.oikvpqya.apps.music.model.PlaybackRepeatMode
import me.oikvpqya.apps.music.model.PlaybackState
import me.tatarka.inject.annotations.Inject

@Inject
class AppMediaControllerImpl(
    private val context: Context,
    private val databaseRepository: AppDatabaseRepository,
    private val preferenceRepository: PreferenceRepository
) : AppMediaController {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val sessionToken by lazy {
        SessionToken(context, ComponentName(context, AppMediaLibraryService::class.java))
    }

    private val mediaControllerDefer by lazy {
        MediaController.Builder(
            context,
            sessionToken
        ).buildAsync().asDeferred()
    }

    private val _playbackSharedFlow = MutableSharedFlow<PlaybackState>(replay = 1)
    private val playbackSharedFlow = _playbackSharedFlow.asSharedFlow()
    private val _playWhenReadySharedFlow = MutableSharedFlow<Boolean>(replay = 1)
    private val playWhenReadySharedFlow = _playWhenReadySharedFlow.asSharedFlow()
    private val _mediaItemTransitionSharedFlow = MutableSharedFlow<Unit>(replay = 1)
    private val mediaItemTransitionSharedFlow = _mediaItemTransitionSharedFlow.asSharedFlow()
    private val _requestAppMediaInfoSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)
    private val requestAppMediaInfoSharedFlow = _requestAppMediaInfoSharedFlow.asSharedFlow()

    private val listener by lazy {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                scope.launch {
                    _playbackSharedFlow.emit(playbackState.asPlaybackState())
                }
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                scope.launch {
                    _playWhenReadySharedFlow.emit(playWhenReady)
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                scope.launch {
                    _mediaItemTransitionSharedFlow.emit(Unit)
                }
            }
        }
    }

    private val mediaControllerFlow: Flow<MediaController> = flow {
        emit(
            mediaControllerDefer.await()
                .also {
                    val playbackState = it.playbackState.asPlaybackState()
                    val playWhenReady = it.playWhenReady
                    val repeatMode = it.repeatMode.asPlaybackRepeatMode()
                    scope.launch {
                        _playbackSharedFlow.emit(playbackState)
                        _playWhenReadySharedFlow.emit(playWhenReady)
                        _mediaItemTransitionSharedFlow.emit(Unit)
                        _requestAppMediaInfoSharedFlow.emit(Unit)
                    }
                }
                .apply {
                    addListener(listener)
                }
        )
    }

    override val mediaInfoFlow: Flow<AppMediaInfo> = combine(
        mediaControllerFlow,
        combine(
            playbackSharedFlow,
            playWhenReadySharedFlow,
            mediaItemTransitionSharedFlow,
            requestAppMediaInfoSharedFlow
        ) { playback, _, _, _ ->
            playback
        },
        databaseRepository.queueFlow,
        preferenceRepository.queueIndexFlow,
        combine(
            preferenceRepository.repeatModeFlow,
            preferenceRepository.shuffleModeFlow
        ) { repeatMode, shuffleMode ->
            repeatMode to shuffleMode
        }
    ) { mediaController, playback, queue, index, modePair ->

        if (playback == PlaybackState.READY) {
            with(mediaController) {
                val tag = currentMediaItem?.asTag()
                AppMediaInfo(
                    isPlaying = isPlaying,
                    song = if (tag != null) Library.Song.Default(tag = tag) else null,
                    position = currentPosition,
                    progress = currentPosition.toFloat() / duration,
                    state = playback,
                    queue = queue,
                    queueIndex = currentMediaItemIndex,
                    repeatMode = modePair.first,
                    shuffleMode = modePair.second,
                    volume = volume,
                )
                    .also {
                        if (it.isPlaying) {
                            scope.launch {
                                delay(1000L)
                                _requestAppMediaInfoSharedFlow.emit(Unit)
                            }
                        }
                    }
            }
        } else {
            AppMediaInfo(
                isPlaying = false,
                song = null,
                position = 0L,
                progress = 0f,
                state = playback,
                queue = queue,
                queueIndex = index,
                repeatMode = modePair.first,
                shuffleMode = modePair.second,
                volume = 1f,
            )
        }
    }

    override val mediaHandlerFlow: Flow<AppMediaHandler> = mediaControllerFlow.map { mediaController ->
        object : AppMediaHandler {
            override fun playOrPause() {
                scope.launch {
                    mediaController.repeatMode
                    with(mediaController) {
                        if (!isConnected) return@launch
                        if (isPlaying) pause() else play()
                    }
                }
            }

            override fun playSongs(items: List<Library.Song>, index: Int, position: Long) {
                if (items.isEmpty()) return
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        setMediaItems(items.map { it.tag.asMediaItem() }, index, position)
                        prepare()
                        play()
//                    setCurrentQueue(items)
                    }
                }
            }

            override fun playAt(index: Int, position: Long) {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        if (currentMediaItem == null) {
                            preferenceRepository.updateQueueIndex(index)
                        } else {
                            seekTo(index, position)
                        }
                    }
                }
            }

            override fun playAt(position: Long) {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        if (isCurrentMediaItemSeekable) {
                            seekTo(position)
                        }
                    }
                }
            }

            override fun like(item: Library.Song?) {
                scope.launch {
                    item?.tag?.mediaId?.let { databaseRepository.setFavorite(listOf(it)) }
                }
            }

            override fun unlike(item: Library.Song?) {
                scope.launch {
                    item?.tag?.mediaId?.let { databaseRepository.deleteFavorite(listOf(it)) }
                }
            }

            override fun isFavoriteSharedFlow(item: Library.Song?): SharedFlow<Boolean> {
                val flow = item?.tag?.mediaId?.let { databaseRepository.isFavoriteFlow(it) }
                return (flow ?: flow {})
                    .shareIn(
                        scope = scope,
                        started = SharingStarted.WhileSubscribed(),
                        replay = 1
                    )
            }

            override fun disableRepeatMode() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        repeatMode = PlaybackRepeatMode.OFF.asInt()
                    }
                }
            }

            override fun enableRepeatOneMode() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        repeatMode = PlaybackRepeatMode.ONE.asInt()
                    }
                }
            }

            override fun enableRepeatAllMode() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        repeatMode = PlaybackRepeatMode.ALL.asInt()
                    }
                }
            }

            override fun enableShuffleMode() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        shuffleModeEnabled = true
                    }
                }
            }

            override fun disableShuffleMode() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        shuffleModeEnabled = false
                    }
                }
            }

            override fun seekToPrevious() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        seekToPrevious()
                    }
                }
            }

            override fun seekToNext() {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        seekToNextMediaItem()
                    }
                }
            }

            override fun setVolume(value: Float) {
                scope.launch {
                    with(mediaController) {
                        if (!isConnected) return@launch
                        volume = value.coerceIn(0f..1f)
                    }
                }
            }
        }
    }
}
