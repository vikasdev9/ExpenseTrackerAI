package com.example.expensetrackerai.features.analytics

import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.model.TransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancialHealthManager @Inject constructor() {

    /**
     * Calculates a financial health score from 0 to 100.
     * Factors: Savings rate, budget adherence, and consistency.
     */
    fun calculateHealthScore(transactions: List<Transaction>, monthlyIncome: Double): Int {
        if (monthlyIncome <= 0) return 0
        
        val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val savingsRate = (monthlyIncome - totalExpense) / monthlyIncome
        
        // Base score on savings rate (50% weight)
        var score = (savingsRate * 50).toInt().coerceIn(0, 50)
        
        // Add consistency points (e.g., number of categories tracked)
        val categories = transactions.map { it.category }.distinct().size
        score += (categories * 5).coerceIn(0, 25)
        
        // Adherence points (placeholder for actual budget logic)
        score += 25 
        
        return score.coerceIn(0, 100)
    }
}
