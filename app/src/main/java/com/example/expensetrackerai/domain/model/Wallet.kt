package com.example.expensetrackerai.domain.model

data class Wallet(
    val id: Long = 0,
    val name: String,
    val balance: Double,
    val type: WalletType,
    val currency: String = "USD"
)

enum class WalletType {
    CASH, BANK_ACCOUNT, CREDIT_CARD, INVESTMENT
}
