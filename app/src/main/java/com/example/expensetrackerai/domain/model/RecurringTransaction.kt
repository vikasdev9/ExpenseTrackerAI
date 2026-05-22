package com.example.expensetrackerai.domain.model

import java.util.Date

data class RecurringTransaction(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: RecurrenceFrequency,
    val nextDate: Date,
    val type: TransactionType,
    val walletId: Long
)

enum class RecurrenceFrequency {
    DAILY, WEEKLY, MONTHLY, YEARLY
}
