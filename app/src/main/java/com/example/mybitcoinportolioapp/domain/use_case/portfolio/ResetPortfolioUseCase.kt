package com.example.mybitcoinportolioapp.domain.use_case.portfolio

import com.example.mybitcoinportolioapp.domain.repository.InvestmentRepository
import com.example.mybitcoinportolioapp.domain.repository.PortfolioRepository

class ResetPortfolioUseCase(
    private val portfolioRepository: PortfolioRepository,
    private val investmentRepository: InvestmentRepository
) {
    suspend operator fun invoke() {
        // Reset portfolio
        portfolioRepository.updatePortfolio(
            totalCash = 20000.0,
            totalInvestment = 0.0,
            lastUpdated = System.currentTimeMillis()
        )
        // Clear all investments
        investmentRepository.clearInvestments()
    }
}