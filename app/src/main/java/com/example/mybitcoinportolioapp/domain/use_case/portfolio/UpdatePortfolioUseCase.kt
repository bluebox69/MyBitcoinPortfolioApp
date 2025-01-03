package com.example.mybitcoinportolioapp.domain.use_case.portfolio

import com.example.mybitcoinportolioapp.data.local.entities.PortfolioEntity
import com.example.mybitcoinportolioapp.domain.repository.PortfolioRepository

class UpdatePortfolioUseCase(
    private val portfolioRepository: PortfolioRepository
) {
    suspend operator fun invoke(totalCash: Double, totalInvestment: Double, lastUpdated: Long) {
        val portfolio = portfolioRepository.getPortfolio()
        val updatedPortfolio = portfolio?.copy(
            totalCash = totalCash,
            totalInvestment = totalInvestment,
            lastUpdated = lastUpdated
        ) ?: PortfolioEntity(totalCash = totalCash, totalInvestment = totalInvestment, lastUpdated = lastUpdated)
        portfolioRepository.updatePortfolio(updatedPortfolio.totalCash, updatedPortfolio.totalInvestment, updatedPortfolio.lastUpdated)
    }
}
