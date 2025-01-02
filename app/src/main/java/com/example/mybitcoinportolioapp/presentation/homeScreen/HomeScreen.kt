package com.example.mybitcoinportolioapp.presentation.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel



@Composable
fun HomeScreen(
    viewModel: CoinViewModel = koinViewModel() // get ViewModel
) {
    Column {
        Text(text = "Start Capital: €98.000")
        Text(text = "BTC Holdings: 1.4 BTC")
        Text(text = "Average Price: €564564")

        // Buttons
        /*
        Button(onClick = { viewModel.updatePortfolio(20000.0, 0.0) }) {
            Text(text = "Initialize Portfolio")
        }

        Button(onClick = { viewModel.fetchCoins() }) {
            Text(text = "Fetch Coins")

        }
         */
    }
}
