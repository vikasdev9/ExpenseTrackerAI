package com.example.expensetrackerai.features.dashboard

import com.example.expensetrackerai.domain.model.Transaction

data class DashboardState(
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val aiInsight: String? = null,
    val budgetProgress: Float = 0f // 0.0 to 1.0
)
