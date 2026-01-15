package kektor.innowise.gallery.activity.msg

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Positive
import java.time.Instant

data class CommentEventMsg(
    override val eventType: EventType,
    @Positive override val userId: Long,
    @Positive override val imageId: Long,
    @Past override val instant: Instant,
    @Positive val commentId: Long,
    @NotBlank val username: String,
    @NotBlank val comment: String
) : EventMsg