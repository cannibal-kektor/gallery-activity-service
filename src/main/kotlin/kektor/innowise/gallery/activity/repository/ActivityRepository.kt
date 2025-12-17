package kektor.innowise.gallery.activity.repository

import kektor.innowise.gallery.activity.model.ActivityEvent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository: MongoRepository<ActivityEvent, String> {
}