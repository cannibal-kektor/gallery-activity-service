package kektor.innowise.gallery.activity.msg

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.time.Instant

data class LikeEventMsg  (
    override val eventType: EventType,
    @Positive override val imageId: Long,
    @Positive override val userId: Long,
    @Past override val instant: Instant,
    @NotBlank val username: String,
    @Positive val imageOwnerId: Long,
    @PositiveOrZero val likesCount: Int
) : EventMsg