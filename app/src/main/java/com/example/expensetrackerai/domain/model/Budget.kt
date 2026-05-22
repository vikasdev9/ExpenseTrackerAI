package com.example.expensetrackerai.domain.model

data class Budget(
    val id: Long = 0,
    val category: String,
    val limit: Double,
    val spent: Double = 0.0,
    val month: Int,
    val year: Int
)
