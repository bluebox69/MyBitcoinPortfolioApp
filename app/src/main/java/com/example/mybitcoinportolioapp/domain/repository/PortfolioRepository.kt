package com.example.mybitcoinportolioapp.domain.repository

import com.example.mybitcoinportolioapp.data.local.entities.PortfolioEntity

interface PortfolioRepository {
    suspend fun getPortfolio(): PortfolioEntity?
    suspend fun updatePortfolio(totalCash: Double, totalInvestment: Double, lastUpdated: Long)
}