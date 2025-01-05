package com.example.mybitcoinportolioapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType

@Entity(tableName = "investment")
data class InvestmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val purchaseType: PurchaseType,  //Typ Buy or Sell
    val quantity: Double,
    val purchasePrice: Double,
    val date: Long,
    val investmentCost: Double
)
