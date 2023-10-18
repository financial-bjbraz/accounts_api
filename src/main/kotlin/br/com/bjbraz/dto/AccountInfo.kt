package br.com.bjbraz.dto

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(value = ["name","cpf","accountNumber", "mainAccountId", "balance", "status", "hasCard"])
class AccountInfo(

        val cpf:String?=null,
        val accountNumber: Int?,
        val mainAccountId: Int?=null,
        val balance: Double?=null,
        val name: String?=null,
        var hasCard:Boolean=true,
        var status:AccountStatus?=null

)