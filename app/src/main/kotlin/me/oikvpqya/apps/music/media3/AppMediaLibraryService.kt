package me.oikvpqya.apps.music.media3

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import kotlinx.coroutines.cancel
import me.oikvpqya.apps.music.app.di.applicationComponent
import me.oikvpqya.apps.music.media3.di.AppMediaLibraryServiceComponent
import me.oikvpqya.apps.music.media3.di.create

class AppMediaLibraryService : MediaLibraryService() {

    val component by lazy(LazyThreadSafetyMode.NONE) {
        AppMediaLibraryServiceComponent::class.create(this, applicationComponent)
    }

    private val player by lazy { component.player }
    private val session by lazy { component.session }
    private val coroutineScope by lazy { component.coroutineScope }

    override fun onDestroy() {
        super.onDestroy()
        session.release()
        player.release()
        coroutineScope.cancel()
    }

    // https://github.com/androidx/media/issues/167#issuecomment-1615184728
    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!isPlaybackOngoing) {
            session.release()
            player.release()
            coroutineScope.cancel()
        }
    }

    // java.lang.IllegalArgumentException: session is already released #422
    // https://github.com/androidx/media/issues/422
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session.takeUnless { session ->
            session.invokeIsReleased
        }.also {
            if (it == null) {
                Log.e(
                    "AppMediaLibraryService",
                    "onGetSession: returns null because the session is already released",
                )
            }
        }
    }
}

// java.lang.IllegalArgumentException: session is already released #422
// https://github.com/androidx/media/issues/422
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
