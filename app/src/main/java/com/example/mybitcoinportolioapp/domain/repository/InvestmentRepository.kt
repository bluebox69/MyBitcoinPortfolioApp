package com.example.mybitcoinportolioapp.domain.repository

import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity

interface InvestmentRepository {
    suspend fun getAllInvestments(): List<InvestmentEntity>
    suspend fun addInvestment(investment: InvestmentEntity)
    suspend fun clearInvestments()
}