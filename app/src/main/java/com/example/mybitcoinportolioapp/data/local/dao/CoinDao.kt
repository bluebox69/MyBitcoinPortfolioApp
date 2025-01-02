package com.example.mybitcoinportolioapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity

@Dao
interface CoinDao {

    // Alle Coins abrufen
    @Query("SELECT * FROM coin LIMIT 1")
    suspend fun getCoin(): CoinEntity?

    // Coins einfügen oder aktualisieren
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: CoinEntity)
}
