package com.example.mybitcoinportolioapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity

@Dao
interface InvestmentDao {

    @Query("SELECT * FROM investment")
    suspend fun getAllInvestments(): List<InvestmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestment(investment: InvestmentEntity)

    @Query("DELETE FROM investment")
    suspend fun clearInvestments()
}
