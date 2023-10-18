package br.com.bjbraz.repository

import br.com.bjbraz.entity.account.AccountEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
//@CacheConfig(cacheNames= ["bank"])
interface BankRepository : ReactiveMongoRepository<AccountEntity, String> {
    fun findByNumber(number:Long) : Mono<AccountEntity>
}