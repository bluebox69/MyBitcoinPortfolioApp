package com.example.mybitcoinportolioapp.presentation.homeScreen

import com.example.mybitcoinportolioapp.domain.model.Coin

data class CoinState(
    val isLoading: Boolean = false,
    val coin: Coin = Coin(
        id = "",
        name = "",
        symbol = "",
        price = 0.0
    ),
    val error: String = ""
)
