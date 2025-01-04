package com.example.mybitcoinportolioapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val totalCash: Double,
    val totalInvestment: Double = 0.0,
    val lastUpdated: Long,
    val coinName: String,
    val coinSymbol: String,
    val totalAmount: Double
)
