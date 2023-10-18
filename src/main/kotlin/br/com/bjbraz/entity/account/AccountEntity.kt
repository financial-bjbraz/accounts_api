package br.com.bjbraz.entity.account

import br.com.bjbraz.dto.AccountStatus
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotEmpty
import kotlin.random.Random

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account")
class AccountEntity (

        @Id
        var _id:ObjectId?=null,
        var number:Long? = null,
        var accountNumber: Int = Random.nextInt(),
        var firebaseId: String?=null,
        var idempotencyKey: String?=null,
        var financialOperationKey:String?=null,

        @NotEmpty
        var name:String? = null,

        var user: PeopleEntity?=null,

        var owner: PeopleEntity?=null,

        var dt_creation: Date?=null,

        var dt_account_close: Date?=null,

        var requestCard:Boolean=true,

        var status: AccountStatus = AccountStatus.CREATED
) : Serializable