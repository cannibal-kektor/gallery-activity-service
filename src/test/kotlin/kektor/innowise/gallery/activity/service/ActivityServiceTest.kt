package kektor.innowise.gallery.activity.service

import kektor.innowise.gallery.activity.mapper.ActivityMapper
import kektor.innowise.gallery.activity.model.ActivityEvent
import kektor.innowise.gallery.activity.msg.EventType
import kektor.innowise.gallery.activity.msg.LikeEventMsg
import kektor.innowise.gallery.activity.repository.ActivityRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant

@ExtendWith(MockitoExtension::class)
class ActivityServiceTest {

    @Mock
    lateinit var activityRepository: ActivityRepository

    @Mock
    lateinit var mapper: ActivityMapper

    @InjectMocks
    lateinit var service: ActivityService

    @Test
    fun `test service`() {
        whenever(mapper.activityMsgToModel(any()))
            .thenReturn(
                ActivityEvent(
                    "",
                    EventType.LIKE,
                    1L,
                    1L,
                    Instant.now()
                )
            )
        val event = LikeEventMsg(EventType.LIKE,
            1L,
            1L,
            Instant.now(),
            "username",
            1L,
            1)

        service.saveActivityEvent(event)
        verify(activityRepository, atLeastOnce()).save(any())
    }


}