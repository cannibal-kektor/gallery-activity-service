package kektor.innowise.gallery.activity.msg

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Positive
import java.time.Instant

data class CommentEventMsg(
    override val eventType: EventType,
    @field:Positive override val userId: Long,
    @field:Positive override val imageId: Long,
    @field:Past override val instant: Instant,
    @field:Positive val commentId: Long,
    @field:NotBlank val username: String,
    @field:NotBlank val comment: String
) : EventMsg