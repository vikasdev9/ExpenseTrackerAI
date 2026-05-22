package com.example.expensetrackerai.domain.model

import java.util.Date

data class Transaction(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val date: Date,
    val category: String,
    val type: TransactionType,
    val walletId: Long,
    val note: String? = null,
    val tags: List<String> = emptyList(),
    val imageUrl: String? = null
)

enum class TransactionType {
    INCOME, EXPENSE
}
