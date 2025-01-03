package com.example.mybitcoinportolioapp.domain.use_case.portfolio

import com.example.mybitcoinportolioapp.data.local.entities.PortfolioEntity
import com.example.mybitcoinportolioapp.domain.repository.PortfolioRepository

class InitializePortfolioUseCase(
    private val portfolioRepository: PortfolioRepository
) {
    suspend operator fun invoke(): PortfolioEntity {
        // Holt das Portfolio aus der Datenbank
        var portfolio = portfolioRepository.getPortfolio()

        // Wenn kein Portfolio existiert, initialisiere es mit Standardwerten
        if (portfolio == null) {
            portfolioRepository.updatePortfolio(totalCash = 20000.0, totalInvestment = 0.0, lastUpdated = System.currentTimeMillis())
            portfolio = portfolioRepository.getPortfolio()
        }
        return portfolio ?: throw Exception("Failed to initialize portfolio")
    }
}
