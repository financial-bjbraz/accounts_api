package br.com.bjbraz.service

import br.com.bjbraz.dto.AccountInfo
import br.com.bjbraz.dto.AccountStatus
import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.queue.producer.SendMessage
import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.RequestReponseRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*
import kotlin.random.Random


@Service("accountService")
class AccountService(
    private val requestRepository: RequestReponseRepository,
    private val accountRepository: AccountRepository,
    private val sendMessage: SendMessage,
    private val webClient: WebClient
)  {

    private val logger = LoggerFactory.getLogger(AccountService::class.java)

    fun findByNumber(accountNumber: Integer?): Mono<AccountEntity> {
        return accountRepository.findByNumber(accountNumber)
    }

    fun save(account: AccountEntity): Mono<AccountEntity> {
        return accountRepository.save(account)
    }

    fun delete(account: AccountEntity): Mono<Void> {
        account.status = AccountStatus.DELETED
        return accountRepository.delete(account)
    }

    fun getAccountInfo(cpf: String): AccountInfo {
        return AccountInfo(
            cpf = "2222",
            accountNumber = 123,
            mainAccountId = 1,
            balance = 10.90,
            name = "alex",
            hasCard = true,
            status = AccountStatus.CREATED
        )
    }

    fun saveAndSend(account: AccountEntity?) : Mono<AccountInfo> {
        logger.info("Saving account $account")

        if(null == account!!.idempotencyKey){
            account!!.idempotencyKey = UUID.randomUUID().toString()
        }

        logger.info("Saving account $account with idempotencykey ${account.idempotencyKey}")
        account.status = AccountStatus.CREATING
        var accountInfo = AccountInfo("", 1, 1, 1.0, "", false)

        return accountRepository.save(account).doOnSuccess(sendMessage::accountCreated)
            .doOnSuccess {
                accountInfo = AccountInfo(it.user?.identifierDocument?.document, it.accountNumber, 1, 1.0, it.user!!.name, false, it.status)
            }
            .flatMap { Mono.just( accountInfo ) }
    }

}