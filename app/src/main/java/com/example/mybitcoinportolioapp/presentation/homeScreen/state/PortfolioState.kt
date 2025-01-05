package com.example.mybitcoinportolioapp.presentation.homeScreen.state

data class PortfolioState(
    val totalCash: Double = 0.0,
    val totalInvestment: Double = 0.0,
    val lastUpdated: Long = 0L,
    val coinName: String = "",
    val coinSymbol: String = "",
    val totalAmount: Double = 0.0,
    val performancePercentage: Double? = null,
    val isLoading: Boolean = false,
    val error: String = ""

)
