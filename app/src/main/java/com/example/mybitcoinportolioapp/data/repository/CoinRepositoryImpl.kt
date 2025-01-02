package com.example.mybitcoinportolioapp.data.repository

import android.util.Log
import com.example.mybitcoinportolioapp.data.local.dao.CoinDao
import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity
import com.example.mybitcoinportolioapp.data.remote.CoinPaprikaAPI
import com.example.mybitcoinportolioapp.domain.repository.CoinRepository

class CoinRepositoryImpl(
    private val api: CoinPaprikaAPI,
    private val dao: CoinDao

) : CoinRepository {

    override suspend fun getCoinFromDatabase(): CoinEntity? {
        return dao.getCoin()
    }

    override suspend fun getCoin(): CoinEntity {
        // API-Aufruf
        val coinDto = api.getCoin()
        val coinEntity = CoinEntity(
            id = coinDto.id,
            name = coinDto.name,
            symbol = coinDto.symbol,
            price = coinDto.quotes.USD.price
        )

        // Neue Daten in Room speichern
        dao.insertCoin(coinEntity)
        Log.d("CoinRepository", "API Response saved in Database")
        return coinEntity
    }

    override suspend fun refreshCoinFromApi(): CoinEntity {
        val coinDto = api.getCoin()
        val coinEntity = CoinEntity(
            id = coinDto.id,
            name = coinDto.name,
            symbol = coinDto.symbol,
            price = coinDto.quotes.USD.price
        )
        dao.insertCoin(coinEntity) // Speichert neue Daten in Room
        Log.d("CoinRepository", "Refreshed data from API saved in Database")
        return coinEntity
    }
}
