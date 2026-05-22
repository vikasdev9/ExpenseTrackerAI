package com.example.expensetrackerai.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetrackerai.data.local.dao.TransactionDao
import com.example.expensetrackerai.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
