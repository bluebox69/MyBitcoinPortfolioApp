package com.example.mybitcoinportolioapp.presentation.buyScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybitcoinportolioapp.R
import com.example.mybitcoinportolioapp.common.toReadableDate
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import com.example.mybitcoinportolioapp.presentation.homeScreen.component.CustomButton
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightWhite
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightYellow
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyScreen(
    viewModel: BuyViewModel = koinViewModel()
) {
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investmentState = viewModel.investmentsState.value
    val calculatedBitcoinAmount = viewModel.calculatedBitcoinAmount.value

    val scrollState = rememberScrollState()
    val decimalFormat = DecimalFormat("#,##0.00")
    val decimalFormat2 = DecimalFormat("#,##0.00000")
    val lastUpdate = portfolioState.lastUpdated

    var investAmount by remember { mutableStateOf("") }

    val context = LocalContext.current
    val toastMessage = viewModel.toastMessageState.value
    if (toastMessage.isNotEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Cash Amount",
                fontSize = 32.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${decimalFormat.format(portfolioState.totalCash)}",
                fontSize = 45.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "1 BTC = $${decimalFormat2.format(coinState.coin.price)}",
                    fontSize = 28.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                IconButton(onClick = { viewModel.refreshPortfolio() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_refresh),
                        contentDescription = "Refresh Coin Data",
                        modifier = Modifier.size(34.dp),
                        tint = Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Last Update: ${lastUpdate.toReadableDate()}",
                    fontSize = 14.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = investAmount,
                onValueChange = { newText ->
                    // Validieren
                    if (newText.all { it.isDigit() || it == '.' }) {
                        investAmount = newText
                        val dollarAmount = newText.toDoubleOrNull() ?: 0.0
                        viewModel.calculateBitcoinAmount(dollarAmount)
                    }
                },
                label = { Text("Enter Dollar Amount to Invest") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = "You will receive approximately ${
                    decimalFormat2.format(
                        calculatedBitcoinAmount
                    )
                } BTC!",
                fontSize = 15.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                buttonText = "Confirm",
                buttonColor = LightYellow,
                painter = painterResource(id = R.drawable.check_broken),
                modifier = Modifier
                    .fillMaxWidth(),
                buttonOnClick = {
                    val dollarAmount = investAmount.toDoubleOrNull() ?: 0.0
                    if (dollarAmount > 0) {
                        viewModel.addInvestment(
                            investmentAmountInDollars = dollarAmount,
                            purchaseType = PurchaseType.BUY
                        )
                    } else {
                        Log.d("BuyScreen", "Invalid investment amount.")
                    }
                }
            )
        }
    }
}