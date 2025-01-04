package com.example.mybitcoinportolioapp.domain.use_case.investment

import android.util.Log
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
        val portfolio = portfolioRepository.getPortfolio()


        if (portfolio != null) {
            val requiredCash = quantity * purchasePrice

            // Überprüfen, ob genug Cash vorhanden ist
            if (portfolio.totalCash < requiredCash) {
                Log.d(
                    "AddInvestmentUseCase",
                    "Insufficient funds: Required = $requiredCash, Available = ${portfolio.totalCash}"
                )
                return // Abbruch
            }

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
            portfolio?.let {
                val updatedCash = it.totalCash - (quantity * purchasePrice)
                val updatedInvestment = it.totalInvestment + (quantity * purchasePrice)
                val updatedAmount = it.totalAmount + quantity
                portfolioRepository.updatePortfolio(
                    updatedCash,
                    updatedInvestment,
                    lastUpdated = System.currentTimeMillis(),
                    coinName = "Bitcoin",
                    coinSymbol = "BTC",
                    updatedAmount
                )
            }
        } else {
            Log.d("AddInvestmentUseCase", "Portfolio not initialized. Unable to add investment.")
        }
    }
}
