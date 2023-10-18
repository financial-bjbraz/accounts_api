package br.com.bjbraz.configuration

import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.rabbit.connection.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.retry.backoff.ExponentialBackOffPolicy

import org.springframework.retry.support.RetryTemplate
import org.springframework.messaging.converter.*
import org.springframework.retry.backoff.Sleeper


@Configuration
class QueueConfiguration(
    @Value("\${app.rabbit.host}") val host: String,
    @Value("\${app.rabbit.user}") val user: String,
    @Value("\${app.rabbit.password}") val password: String,
    @Value("\${app.rabbit.port}") val port: Int

    ) {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val retorno = CachingConnectionFactory(host, port)
        retorno.username = user
        retorno.setPassword(password)
        return retorno
    }

    @Bean
    fun amqpAdmin(): RabbitAdmin? {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        var retorno = RabbitTemplate(connectionFactory())
        val retryTemplate = RetryTemplate()
        retorno.messageConverter = jsonMessageConverter();
        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = 5000
        backOffPolicy.multiplier = 10.0
        backOffPolicy.maxInterval = 10000
        backOffPolicy.setSleeper(Sleeper { 10000 })
        retryTemplate.setBackOffPolicy(backOffPolicy)
        retorno.setRetryTemplate(retryTemplate)

        return retorno
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }


//    @Bean
//    fun queueAccountsCreation(): Queue {
//        return Queue("accounts_creation")
//    }
//
//    @Bean
//    fun queueAccountsCreated(): Queue {
//        return Queue("accounts_created")
//    }
//
//    @Bean
//    fun queueAccountsUpdated(): Queue {
//        return Queue("accounts_updated")
//    }
}