package me.oikvpqya.apps.music.model

enum class PlaybackRepeatMode {
    OFF,
    ONE,
    ALL
    ;

    override fun toString(): String {
        return when (this) {
            OFF -> "off"
            ONE -> "one"
            ALL -> "all"
        }
    }
}
