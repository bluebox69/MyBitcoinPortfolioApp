package com.example.mybitcoinportolioapp.presentation.homeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun TestHomescreen(
    viewModel: CoinViewModel = koinViewModel() // get ViewModel
) {
    val scrollState = rememberScrollState()
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investments = viewModel.investmentsState.value

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {

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
        Spacer(modifier = Modifier.height(16.dp))
        // Display Portfolio
        Text(text = "Dein Portfolio:")
        if (portfolioState.isLoading) {
            Text(text = "Loading Portfolio...")
        } else if (portfolioState.error.isNotEmpty()) {
            Text(text = "Portfolio Error: ${portfolioState.error}")
        } else {
            Text(text = "Total Cash: $${String.format("%.2f", portfolioState.totalCash)}")
            Text(text = "Total Investment: $${String.format("%.2f", portfolioState.totalInvestment)}")
            Text(text = "Total Amount: ${portfolioState.totalAmount}")
            Text(text = "Verlauf: ${portfolioState.performancePercentage}")
            Text(text = "Last Update: ${portfolioState.lastUpdated.toReadableDate()}")
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Buttons
        Button(onClick = { viewModel.getCoin() }) {
            Text(text = "Fetch Coin")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.addInvestment(coinState.coin, 0.1, coinState.coin.price, PurchaseType.BUY) }) {
            Text(text = "Add Investment")
        }
        Button(onClick = { viewModel.addInvestment(coinState.coin, 0.1, coinState.coin.price, PurchaseType.SELL) }) {
            Text(text = "Sell Investment")
        }
        Button(onClick = { viewModel.resetPortfolio() }) {
            Text(text = "Reset Portfolio")
        }
    }
}