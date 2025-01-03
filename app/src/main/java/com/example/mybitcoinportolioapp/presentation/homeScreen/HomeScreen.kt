package com.example.mybitcoinportolioapp.presentation.homeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybitcoinportolioapp.common.toReadableDate
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import org.koin.androidx.compose.koinViewModel

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    viewModel: CoinViewModel = koinViewModel() // get ViewModel
) {
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investments = viewModel.investmentsState.value

    Column {

        Text(text = "Aktueller Bitcoin Kurs:")
        if (coinState.isLoading) {
            Text(text = "Loading...")

        } else if (coinState.error.isNotEmpty()) {
            Text(text = "Error: ${coinState.error}")
        } else {
            coinState.coin.let { coin ->
                Text(text = "Id: ${coin.id}")
                Text(text = "Name: ${coin.name}")
                Text(text = "Symbol: ${coin.symbol}")
                Text(text = "Price: $${coin.price}")
            }
        }
        // Display Portfolio
        Text(text = "Dein Portfolio:")
        if (portfolioState.isLoading) {
            Text(text = "Loading Portfolio...")
        } else if (portfolioState.error.isNotEmpty()) {
            Text(text = "Portfolio Error: ${portfolioState.error}")
        } else {
            Text(text = "Total Cash: €${String.format("%.2f", portfolioState.totalCash)}")
            Text(text = "Total Investment: €${String.format("%.2f", portfolioState.totalInvestment)}")
            Text(text = "Last Update: ${portfolioState.lastUpdated.toReadableDate()}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Investments
        Text(text = "Investments:")
        if (investments.isEmpty()) {
            Text(text = "No investments available.")
        } else {
            investments.forEach { investment ->
                Text(text = "Coin: ${investment.coinName} (${investment.coinSymbol})")
                Text(text = "Quantity: ${investment.quantity}")
                Text(text = "Price: €${investment.purchasePrice}")
                Text(text = "Type: ${investment.purchaseType}")
                Text(text = "Type: ${investment.date.toReadableDate()}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Buttons
        Button(onClick = { viewModel.getCoin() }) {
            Text(text = "Fetch Coin")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.addInvestment(coinState.coin, 0.01, coinState.coin.price, PurchaseType.BUY) }) {
            Text(text = "Add Investment")
        }
        Button(onClick = { viewModel.initializePortfolio() }) {
            Text(text = "Reset Portfolio")
        }
    }
}