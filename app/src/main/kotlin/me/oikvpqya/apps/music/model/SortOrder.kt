package me.oikvpqya.apps.music.model

enum class SortOrder {
    ASCENDING,
    DESCENDING
    ;

    override fun toString(): String {
        return when (this) {
            ASCENDING -> "ASC"
            DESCENDING -> "DESC"
        }
    }
}
