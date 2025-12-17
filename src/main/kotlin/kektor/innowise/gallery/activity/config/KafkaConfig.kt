package kektor.innowise.gallery.activity.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafkaRetryTopic
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.ContainerCustomizer
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@EnableKafkaRetryTopic
@Configuration
class KafkaConfig(
    private var validator: LocalValidatorFactoryBean
) : KafkaListenerConfigurer {

    @Value("#{'\${app.broker.social-events-topics}'.split(',')}")
    lateinit var socialEventsTopics: Array<String>

    @Profile("!smoke")
    @Bean
    fun socialEventsTopic(): KafkaAdmin.NewTopics {
        val topics = socialEventsTopics.map {
            TopicBuilder.name(it)
                .partitions(1)
                .replicas(1)
                .build()
        }.toTypedArray()
        return KafkaAdmin.NewTopics(*topics)
    }

    @Bean
    fun kafkaContainerCustomizer(executor: ThreadPoolTaskExecutor)
            : ContainerCustomizer<Any, Any, ConcurrentMessageListenerContainer<Any, Any>> {
        return ContainerCustomizer { container: ConcurrentMessageListenerContainer<Any, Any> ->
            container.containerProperties.listenerTaskExecutor = executor
        }
    }

    @Bean
    fun taskScheduler(): TaskScheduler {
        return ThreadPoolTaskScheduler()
    }

    override fun configureKafkaListeners(registrar: KafkaListenerEndpointRegistrar) {
        registrar.setValidator(validator)
    }
}