package com.example.mybitcoinportolioapp.domain.repository

import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity

interface CoinRepository {

    suspend fun getCoinFromDatabase(): CoinEntity? // Daten aus Room
    suspend fun getCoin(): CoinEntity // Daten von der API
    suspend fun refreshCoinFromApi(): CoinEntity // Daten von der API in Room aktualisieren

}