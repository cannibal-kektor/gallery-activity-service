package kektor.innowise.gallery.activity.model

import kektor.innowise.gallery.activity.msg.EventType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "activity")
data class ActivityEvent(
    @Id val id: String? = null,
    val eventType: EventType,
    val imageId: Long,
    val userId: Long,
    val createdAt: Instant
)
