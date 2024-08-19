package me.oikvpqya.apps.music.model

enum class PlaybackState {
    IDLE,
    BUFFERING,
    READY,
    ENDED
    ;

    override fun toString(): String {
        return when (this) {
            IDLE -> "IDLE"
            BUFFERING -> "BUFFERING"
            READY -> "READY"
            ENDED -> "ENDED"
        }
    }
}
