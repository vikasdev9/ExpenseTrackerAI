package com.example.expensetrackerai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetrackerai.domain.model.TransactionType
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val date: Date,
    val category: String,
    val type: TransactionType,
    val note: String?,
    val imageUrl: String?
)
