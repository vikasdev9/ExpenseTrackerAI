package com.example.expensetrackerai.features.ai

import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.model.TransactionType
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpendingPredictor @Inject constructor() {

    /**
     * Predicts total spending for the current month based on historical data.
     * Uses a simple linear trend projection.
     */
    fun predictMonthlySpending(transactions: List<Transaction>): Double {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        if (expenses.isEmpty()) return 0.0

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val totalSpentSoFar = expenses.sumOf { it.amount }
        val dailyAverage = totalSpentSoFar / currentDay

        return dailyAverage * totalDaysInMonth
    }

    /**
     * Detects unusual spending behavior.
     */
    fun detectUnusualSpending(newTransaction: Transaction, history: List<Transaction>): Boolean {
        val sameCategoryExpenses = history.filter { 
            it.type == TransactionType.EXPENSE && it.category == newTransaction.category 
        }
        if (sameCategoryExpenses.size < 5) return false

        val average = sameCategoryExpenses.sumOf { it.amount } / sameCategoryExpenses.size
        // Threshold: 2x the average for that category
        return newTransaction.amount > (average * 2)
    }
}
