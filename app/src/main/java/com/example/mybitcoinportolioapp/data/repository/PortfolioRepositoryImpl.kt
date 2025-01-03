package com.example.mybitcoinportolioapp.data.repository

import com.example.mybitcoinportolioapp.data.local.dao.PortfolioDao
import com.example.mybitcoinportolioapp.data.local.entities.PortfolioEntity
import com.example.mybitcoinportolioapp.domain.repository.PortfolioRepository

class PortfolioRepositoryImpl(
    private val dao: PortfolioDao
) : PortfolioRepository {
    override suspend fun getPortfolio(): PortfolioEntity? = dao.getPortfolio()

    override suspend fun updatePortfolio(totalCash: Double, totalInvestment: Double, lastUpdated: Long) {
        val currentPortfolio = dao.getPortfolio()

        if (currentPortfolio != null) {
            // Aktualisiere nur die benötigten Felder
            dao.updatePortfolioFields(
                id = currentPortfolio.id,
                totalCash = totalCash,
                totalInvestment = totalInvestment,
                lastUpdated = lastUpdated
            )
        } else {
            // Wenn kein Portfolio existiert, ein neues erstellen
            val newPortfolio = PortfolioEntity(
                totalCash = totalCash,
                totalInvestment = totalInvestment,
                lastUpdated = lastUpdated
            )
            dao.insertOrUpdatePortfolio(newPortfolio)
        }
    }
}