package kektor.innowise.gallery.activity.msg

import java.time.Instant

sealed interface EventMsg {
    val eventType: EventType
    val userId: Long
    val imageId: Long
    val instant: Instant
}

enum class EventType {
    LIKE,
    REMOVE_LIKE,
    ADD_COMMENT,
    UPDATE_COMMENT,
    REMOVE_COMMENT
}