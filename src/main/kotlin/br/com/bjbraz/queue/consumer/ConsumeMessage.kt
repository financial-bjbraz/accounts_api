package br.com.bjbraz.queue.consumer

import br.com.bjbraz.dto.AccountStatus
import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.repository.AccountRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConsumeMessage(private val accountRepository: AccountRepository) {
    private val logger = LoggerFactory.getLogger(ConsumeMessage::class.java)

    @RabbitListener(queues = ["accounts_created"])
    fun listen(id: AccountEntity) {
        try {
            accountRepository.findById(id._id!!)
                .flatMap { setStatus(it) }
                .flatMap { accountRepository.save(it) }

        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            logger.error("Error receiving message $ex")
            logger.error("Error receiving message $ex")
            logger.error("Error receiving message $ex")
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    fun setStatus(account: AccountEntity): Mono<AccountEntity> {
        account.status = AccountStatus.CREATED
        return Mono.from { account }
    }

}