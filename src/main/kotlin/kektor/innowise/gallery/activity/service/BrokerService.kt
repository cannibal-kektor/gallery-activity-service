package kektor.innowise.gallery.activity.service

import jakarta.validation.Valid
import kektor.innowise.gallery.activity.msg.EventMsg
import mu.KotlinLogging
import org.springframework.kafka.annotation.BackOff
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class BrokerService(
    val activityService: ActivityService
) {

    @RetryableTopic(
        attempts = "3",
        concurrency = "1",
        backOff = BackOff(
            delay = 1000,
            multiplier = 2.0
        )
    )
    @KafkaListener(
        topics = ["#{'\${app.broker.social-events-topics}'.split(',')}"],
        groupId = "\${app.broker.group-id}",
        clientIdPrefix = "\${app.hostname}-social-events-consumer",
        concurrency = "5"
    )
    fun listenSocialActivitiesEvents(@Payload @Valid msg: EventMsg) =
        activityService.saveActivityEvent(msg)


    @DltHandler
    fun handleDlt(
        @Payload msg: EventMsg,
        @Header(KafkaHeaders.EXCEPTION_FQCN) exceptionName: String?,
        @Header(KafkaHeaders.EXCEPTION_CAUSE_FQCN) exceptionCause: String?,
        @Header(KafkaHeaders.EXCEPTION_MESSAGE) exceptionMessage: String?,
        @Header(KafkaHeaders.EXCEPTION_STACKTRACE) exceptionStackTrace: String?
    ) = logger.error {
        """
            DLT record received : 
            Exception name=$exceptionName
            Exception cause=$exceptionCause
            Exception message=$exceptionMessage
            Exception stacktrace=$exceptionStackTrace
            record=$msg
            """.trimIndent()
    }
}