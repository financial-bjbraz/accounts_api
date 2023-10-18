package br.com.bjbraz.entity.account

import java.io.Serializable


class IdentifierDocumentEntity(
        val idDocument: Long??=null,

        val document:String?=null,

        val type:String="CPF"): Serializable

/*
"identifierDocument": {
    "document": "51005633037",
    "type": "CPF"
},
*/