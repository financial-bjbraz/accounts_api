package br.com.bjbraz.dto


enum class AccountStatus(val nome:String) {
    CREATING("CREATING"),
    CREATED("CREATED"),
    UNBLOCKED("UNBLOCKED"),
    BLOCKED("BLOCKED"),
    DELETED("DELETED")
}