package com.example.expensetrackerai.domain.usecase

import com.example.expensetrackerai.domain.model.Transaction
import com.example.expensetrackerai.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getAllTransactions()
    }
}
