package com.example.expensetrackerai.features.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.model.TransactionType
import com.example.expensetrackerai.domain.usecase.AddTransactionUseCase
import com.example.expensetrackerai.features.ai.CategorizationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categorizationManager: CategorizationManager
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onTitleChange(newTitle: String): String {
        return categorizationManager.categorizeTransaction(newTitle)
    }

    fun onSaveClick(
        title: String,
        amount: String,
        category: String,
        type: TransactionType
    ) {
        viewModelScope.launch {
            if (title.isBlank() || amount.isBlank() || category.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Please fill all fields"))
                return@launch
            }

            val amountDouble = amount.toDoubleOrNull()
            if (amountDouble == null) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Invalid amount"))
                return@launch
            }

            addTransactionUseCase(
                Transaction(
                    title = title,
                    amount = amountDouble,
                    date = Date(),
                    category = category,
                    type = type
                )
            )
            _eventFlow.emit(UiEvent.TransactionSaved)
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object TransactionSaved : UiEvent()
    }
}
