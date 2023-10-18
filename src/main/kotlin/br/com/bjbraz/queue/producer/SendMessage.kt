package br.com.bjbraz.queue.producer

import br.com.bjbraz.entity.account.AccountEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class SendMessage(
    private val template: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(SendMessage::class.java)

    fun accountCreated(account: AccountEntity) {
        try {
            logger.info("exchange_accounts_created sending {} ", account._id)
//            val message = MessageBuilder.withPayload(account)
//                .setHeader(MessageHeaders.CONTENT_TYPE, "application/xml")
//                .setHeader("teste", "teste")
//                .setCorrelationId(UUID.randomUUID().toString())
//                .build()

            template.convertAndSend("exchange_accounts_created","", account)
//            template.convertAndSend("exchange_accounts_creation","", account)

//            template.send("exchange_accounts_created", org.springframework.amqp.core.Message(account))
        } catch (ex: Exception) {
            logger.error("Error sending message $ex")
            throw ex
        }
    }


}