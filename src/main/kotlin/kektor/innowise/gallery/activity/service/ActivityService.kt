package kektor.innowise.gallery.activity.service

import kektor.innowise.gallery.activity.mapper.ActivityMapper
import kektor.innowise.gallery.activity.msg.EventMsg
import kektor.innowise.gallery.activity.repository.ActivityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val mapper: ActivityMapper
) {

    @Transactional
    fun saveActivityEvent(eventMsg: EventMsg) {
        val model = mapper.activityMsgToModel(eventMsg)
        activityRepository.save(model)
    }

}