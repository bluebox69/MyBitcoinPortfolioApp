package com.example.mybitcoinportolioapp.presentation.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel



@Composable
fun HomeScreen(
    viewModel: CoinViewModel = koinViewModel() // get ViewModel
) {
    val state = viewModel.state.value

    Column {

        Text(text = "Aktueller Bitcoin Kurs:")
        if (state.isLoading) {
            Text(text = "Loading...")

        } else if (state.error.isNotEmpty()) {
            Text(text = "Error: ${state.error}")
        } else {
            state.coin.let { coin ->
                Text(text = "Id: ${coin.id}")
                Text(text = "Name: ${coin.name}")
                Text(text = "Symbol: ${coin.symbol}")
                Text(text = "Price: $${coin.price}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Dein Portfolio:")
        Text(text = "Start Capital: €98.000")
        Text(text = "BTC Holdings: 1.4 BTC")
        Text(text = "Average Price: €564564")

        // Buttons
        Button(onClick = { viewModel.refreshCoin() }) {
            Text(text = "Fetch Coin")

        }
    }
}
