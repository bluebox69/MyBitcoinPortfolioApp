package com.example.mybitcoinportolioapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mybitcoinportolioapp.domain.model.Coin

@Entity(tableName = "coin")
data class CoinEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val price: Double
)


