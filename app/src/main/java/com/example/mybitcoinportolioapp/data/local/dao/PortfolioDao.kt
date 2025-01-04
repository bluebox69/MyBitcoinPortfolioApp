package com.example.mybitcoinportolioapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mybitcoinportolioapp.data.local.entities.PortfolioEntity

@Dao
interface PortfolioDao {

    @Query("SELECT * FROM portfolio LIMIT 1")
    suspend fun getPortfolio(): PortfolioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePortfolio(portfolio: PortfolioEntity)

    @Query("UPDATE portfolio SET totalcash = :totalCash, totalinvestment = :totalInvestment, lastUpdated = :lastUpdated, coinName = :coinName, coinSymbol = :coinSymbol, totalAmount = :totalAmount WHERE id = :id")
    suspend fun updatePortfolioFields(id: Int, totalCash: Double, totalInvestment: Double, lastUpdated: Long, coinName: String, coinSymbol: String, totalAmount: Double )
}
