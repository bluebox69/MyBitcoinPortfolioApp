package com.example.mybitcoinportolioapp.domain.repository

import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity
import com.example.mybitcoinportolioapp.domain.model.Coin

interface CoinRepository {

    suspend fun getCoinFromDatabase(): CoinEntity? // Daten aus Room
    suspend fun getCoin(): Coin // Daten von der API

}