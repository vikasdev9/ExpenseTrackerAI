package com.example.expensetrackerai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetrackerai.domain.model.WalletType

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val balance: Double,
    val type: WalletType,
    val currency: String
)
