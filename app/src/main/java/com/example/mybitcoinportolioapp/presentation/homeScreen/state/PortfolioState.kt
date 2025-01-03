package com.example.mybitcoinportolioapp.presentation.homeScreen.state

data class PortfolioState(
    val totalCash: Double = 0.0,
    val totalInvestment: Double = 0.0,
    val lastUpdated: Long = 0L,
    val isLoading: Boolean = false,
    val error: String = ""

)
