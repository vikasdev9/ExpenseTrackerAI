package com.example.expensetrackerai.domain.usecase

import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
}
