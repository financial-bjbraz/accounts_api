package br.com.bjbraz.controller

import br.com.bjbraz.dto.AccountInfo
import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.exception.DuplicatedBankException
import br.com.bjbraz.exception.NotFoundException
import br.com.bjbraz.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/accounts")
@CacheConfig(cacheNames= ["accounts"])
class AccountsController(
    val service: AccountService
) {

    private val logger = LoggerFactory.getLogger(AccountsController::class.java)

//    @GetMapping(value = ["/all"], produces = ["application/json"])
//    fun listAll(): ResponseEntity<Flux<AccountEntity>> {
//        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll())
//    }

    @PutMapping(value = [""], produces = ["application/json"])
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody request: AccountEntity): Mono<AccountInfo> {
        try{

          return service.saveAndSend(request)

        }catch(ex:Exception){
            logger.error("Error finding Bank")
                throw DuplicatedBankException(message = "Mensagem", path = "", logref = "")
        }

    }

    @PatchMapping(value = ["/{idAccount}"], produces = ["application/json"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable accountNumber: Integer, @RequestBody request: AccountEntity): Mono<AccountEntity> {
        try{
            return service
                .findByNumber(accountNumber)
                .switchIfEmpty(
                    Mono.error(NotFoundException)
                )
                .doOnSuccess{ it.name = request.name }
                .flatMap { bank -> service.save(bank) }
        }catch(ex:Exception){
            logger.error("Error finding Bank")
            throw DuplicatedBankException(message = "Mensagem", path = "", logref = "")
        }

    }

    @DeleteMapping(value = ["/{idAccount}"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun closeAccount(@PathVariable accountNumber: Integer): Mono<Void> {
        return service.findByNumber(accountNumber)
            .switchIfEmpty(Mono.error(NotFoundException))
            .flatMap { account -> service.delete(account) }
            .then(Mono.empty())
    }

//    @GetMapping(value = ["/{idAccount}"])
//    @ResponseStatus(value = HttpStatus.OK)
//    fun get(@PathVariable bankNumber: Long): Mono<AccountEntity> {
//        return repository
//            .findByNumber(bankNumber)
//            .switchIfEmpty(
//                Mono.error(NotFoundException)
//            )
//    }

}
