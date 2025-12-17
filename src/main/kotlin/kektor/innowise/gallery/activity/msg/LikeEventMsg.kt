package kektor.innowise.gallery.activity.msg

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.time.Instant

data class LikeEventMsg  (
    override val eventType: EventType,
    @field:Positive override val imageId: Long,
    @field:Positive override val userId: Long,
    @field:Past override val instant: Instant,
    @field:NotBlank val username: String,
    @field:Positive val imageOwnerId: Long,
    @field:PositiveOrZero val likesCount: Int
) : EventMsg