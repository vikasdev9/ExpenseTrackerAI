package com.example.expensetrackerai.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.usecase.GetTransactionsUseCase
import com.example.expensetrackerai.features.ai.SpendingPredictor
import com.example.expensetrackerai.features.analytics.FinancialHealthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getTransactionsUseCase: GetTransactionsUseCase,
    private val spendingPredictor: SpendingPredictor,
    private val healthManager: FinancialHealthManager
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = getTransactionsUseCase()
        .map { transactions ->
            val totalBalance = transactions.sumOf { if (it.type == com.example.expensetrackerai.domain.model.TransactionType.INCOME) it.amount else -it.amount }
            val monthlyIncome = transactions.filter { it.type == com.example.expensetrackerai.domain.model.TransactionType.INCOME }.sumOf { it.amount }
            val monthlyExpense = transactions.filter { it.type == com.example.expensetrackerai.domain.model.TransactionType.EXPENSE }.sumOf { it.amount }
            
            val predictedSpending = spendingPredictor.predictMonthlySpending(transactions)
            val healthScore = healthManager.calculateHealthScore(transactions, monthlyIncome)
            
            val aiInsight = if (predictedSpending > (monthlyIncome * 0.8) && monthlyIncome > 0) {
                "Alert: You're projected to spend 80% of your income. Consider saving more!"
            } else if (monthlyExpense > 0) {
                "Your financial health score is $healthScore. Keep it up!"
            } else {
                "Welcome! Start adding transactions to see AI-powered insights."
            }

            DashboardUiState.Success(
                transactions = transactions,
                totalBalance = totalBalance,
                monthlyIncome = monthlyIncome,
                monthlyExpense = monthlyExpense,
                aiInsight = aiInsight,
                healthScore = healthScore
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState.Loading
        )
}

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(
        val transactions: List<Transaction>,
        val totalBalance: Double,
        val monthlyIncome: Double,
        val monthlyExpense: Double,
        val aiInsight: String,
        val healthScore: Int
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
