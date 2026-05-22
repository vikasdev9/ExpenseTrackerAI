package com.example.expensetrackerai.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetrackerai.data.local.dao.BudgetDao
import com.example.expensetrackerai.data.local.dao.TransactionDao
import com.example.expensetrackerai.data.local.dao.WalletDao
import com.example.expensetrackerai.data.local.entity.BudgetEntity
import com.example.expensetrackerai.data.local.entity.TransactionEntity
import com.example.expensetrackerai.data.local.entity.WalletEntity

@Database(
    entities = [
        TransactionEntity::class,
        WalletEntity::class,
        BudgetEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun walletDao(): WalletDao
    abstract fun budgetDao(): BudgetDao
}
