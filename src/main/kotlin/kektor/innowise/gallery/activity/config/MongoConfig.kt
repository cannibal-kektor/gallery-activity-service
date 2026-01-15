package kektor.innowise.gallery.activity.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter

@Configuration
class MongoConfig {
    @Bean
    fun mongoTransactionManager(dbFactory: MongoDatabaseFactory) =
        MongoTransactionManager(dbFactory)

    @Bean
    fun mongoTemplate(
        factory: MongoDatabaseFactory,
        converter: MappingMongoConverter
    ): MongoTemplate {
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        return MongoTemplate(factory, converter)
    }

}