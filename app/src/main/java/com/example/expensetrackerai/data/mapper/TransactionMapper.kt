package com.example.expensetrackerai.data.mapper

import com.example.expensetrackerai.data.local.entity.TransactionEntity
import com.example.expensetrackerai.domain.model.Transaction

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        title = title,
        amount = amount,
        date = date,
        category = category,
        type = type,
        note = note,
        imageUrl = imageUrl
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        title = title,
        amount = amount,
        date = date,
        category = category,
        type = type,
        note = note,
        imageUrl = imageUrl
    )
}
