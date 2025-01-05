package com.example.mybitcoinportolioapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mybitcoinportolioapp.data.local.dao.CoinDao
import com.example.mybitcoinportolioapp.data.local.dao.InvestmentDao
import com.example.mybitcoinportolioapp.data.local.dao.PortfolioDao
import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity
import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity
import com.example.mybitcoinportolioapp.data.local.entities.PortfolioEntity
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.Converters


@Database(
    entities = [CoinEntity::class, PortfolioEntity::class, InvestmentEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun investmentDao(): InvestmentDao
}