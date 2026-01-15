package kektor.innowise.gallery.activity.mapper

import kektor.innowise.gallery.activity.model.ActivityEvent
import kektor.innowise.gallery.activity.msg.EventMsg
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(config = MapConfig::class)
interface ActivityMapper {

    @Mapping(source = "instant", target = "createdAt")
    fun activityMsgToModel(msg: EventMsg) : ActivityEvent

}