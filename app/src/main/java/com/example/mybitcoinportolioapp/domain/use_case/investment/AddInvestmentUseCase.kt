package com.example.mybitcoinportolioapp.domain.use_case.investment

import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import com.example.mybitcoinportolioapp.domain.model.Coin
import com.example.mybitcoinportolioapp.domain.repository.InvestmentRepository
import com.example.mybitcoinportolioapp.domain.repository.PortfolioRepository

class AddInvestmentUseCase(
    private val investmentRepository: InvestmentRepository,
    private val portfolioRepository: PortfolioRepository
) {
    suspend operator fun invoke(coin: Coin, quantity: Double, purchasePrice: Double, purchaseType: PurchaseType ) {
        val investment = InvestmentEntity(
            coinId = coin.id,
            coinName = coin.name,
            coinSymbol = coin.symbol,
            quantity = quantity,
            purchasePrice = purchasePrice,
            date = System.currentTimeMillis(),
            purchaseType = purchaseType
        )
        investmentRepository.addInvestment(investment)

        // Portfolio aktualisieren
        val portfolio = portfolioRepository.getPortfolio()
        portfolio?.let {
            val updatedCash = it.totalCash - (quantity * purchasePrice)
            val updatedInvestment = it.totalInvestment + (quantity * purchasePrice)
            portfolioRepository.updatePortfolio(updatedCash, updatedInvestment, lastUpdated = System.currentTimeMillis())
        }
    }
}
