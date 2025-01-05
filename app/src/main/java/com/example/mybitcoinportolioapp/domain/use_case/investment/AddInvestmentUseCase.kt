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
            val investmentCosts = quantity * purchasePrice

            // Überprüfen, ob genug Cash vorhanden ist
            if (portfolio.totalCash < investmentCosts && purchaseType == PurchaseType.BUY) {
                Log.d(
                    "AddInvestmentUseCase",
                    "Insufficient funds: Required = $investmentCosts, Available = ${portfolio.totalCash}"
                )
                return // Abbruch
            }
            //Überprüfen ob genügen Coins für den Verkauf vorhanden sind
            if (portfolio.totalAmount < quantity && purchaseType == PurchaseType.SELL) {
                Log.d(
                    "AddInvestmentUseCase",
                    "Insufficient Coin Amount: Required = $quantity, Available = ${portfolio.totalAmount}"
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
                purchaseType = purchaseType,
                investmentCost = investmentCosts
            )
            investmentRepository.addInvestment(investment)

            // Portfolio aktualisieren
            var updatedCash = 0.0
            var updatedInvestment = 0.0
            var updatedAmount = 0.0

            portfolio?.let {
                if (purchaseType == PurchaseType.BUY) {
                    updatedCash = it.totalCash - investmentCosts
                    updatedInvestment = it.totalInvestment + investmentCosts
                    updatedAmount = it.totalAmount + quantity
                }
                if (purchaseType == PurchaseType.SELL) {
                    updatedCash = it.totalCash + investmentCosts
                    updatedInvestment = it.totalInvestment - investmentCosts
                    updatedAmount = it.totalAmount - quantity
                }
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
