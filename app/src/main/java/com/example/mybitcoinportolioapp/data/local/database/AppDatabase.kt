package com.example.mybitcoinportolioapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mybitcoinportolioapp.data.local.dao.CoinDao
import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity


@Database(entities = [CoinEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao
}