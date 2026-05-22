package com.example.expensetrackerai.features.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.model.TransactionType
import com.example.expensetrackerai.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    val uiState: StateFlow<AnalyticsUiState> = getTransactionsUseCase()
        .map { transactions ->
            val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
            val categorySpending = expenseTransactions.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
            
            AnalyticsUiState.Success(
                categorySpending = categorySpending,
                totalExpense = expenseTransactions.sumOf { it.amount }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnalyticsUiState.Loading
        )
}

sealed interface AnalyticsUiState {
    object Loading : AnalyticsUiState
    data class Success(
        val categorySpending: Map<String, Double>,
        val totalExpense: Double
    ) : AnalyticsUiState
}
