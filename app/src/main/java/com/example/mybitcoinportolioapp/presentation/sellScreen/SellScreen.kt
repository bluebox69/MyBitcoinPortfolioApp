package com.example.mybitcoinportolioapp.presentation.sellScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.mybitcoinportolioapp.presentation.global_components.CustomButton
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightWhite
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat
import androidx.compose.ui.platform.LocalContext
import com.example.mybitcoinportolioapp.common.showToast
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightRed


@Composable
fun SellScreen(
    viewModel: SellViewModel = koinViewModel()
) {
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investmentState = viewModel.investmentsState.value
    val calculatedBitcoinAmount = viewModel.calculatedDollarAmount.value

    val scrollState = rememberScrollState()
    val decimalFormat = DecimalFormat("#,##0.00")
    val decimalFormat2 = DecimalFormat("#,##0.00000")
    val lastUpdate = portfolioState.lastUpdated

    var sellAmount by remember { mutableStateOf("") }

    //Toast Message
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()

    //Refresh Rotation
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var rotationTrigger by remember { mutableStateOf(false) }
    var totalRotation by remember { mutableFloatStateOf(0f) }

    // Rotation animation
    val rotationAngle by animateFloatAsState(
        targetValue = totalRotation,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        finishedListener = { rotationTrigger = false }
    )

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            showToast(context, it)
            viewModel.clearToastMessage()
        }
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
                text = "Coin Amount",
                fontSize = 32.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${decimalFormat2.format(portfolioState.totalAmount)} BTC",
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
                    text = "1 BTC = $${decimalFormat.format(coinState.coin.price)}",
                    fontSize = 28.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                IconButton(
                    onClick = {
                        totalRotation += 360f
                        rotationTrigger = true
                        viewModel.refreshPortfolio(forceRefresh = true)
                    },
                    enabled = !isRefreshing
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_refresh),
                        contentDescription = "Refresh Coin Data",
                        modifier = Modifier
                            .size(34.dp)
                            .graphicsLayer(
                                rotationZ = rotationAngle,
                                scaleX = -1f
                            ),
                        tint = if (isRefreshing) Color.Gray else Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Invested: $${decimalFormat.format(portfolioState.totalInvestment)}",
                    fontSize = 28.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
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
                value = sellAmount,
                onValueChange = { newText ->
                    // Validieren
                    if (newText.all { it.isDigit() || it == '.' }) {
                        sellAmount = newText
                        val coinAmount = newText.toDoubleOrNull() ?: 0.0
                        viewModel.calculateBitcoinValue(coinAmount)
                    }
                },
                label = { Text("Enter Coin Amount to Sell") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = "You will receive approximately $${
                    decimalFormat.format(
                        calculatedBitcoinAmount
                    )
                }!",
                fontSize = 15.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                buttonText = "Confirm",
                buttonColor = LightRed,
                painter = painterResource(id = R.drawable.check_broken),
                modifier = Modifier
                    .fillMaxWidth(),
                buttonOnClick = {
                    val coinAmount = sellAmount.toDoubleOrNull() ?: 0.0
                    if (coinAmount > 0) {
                        viewModel.processSellOrder(
                            sellingAmountInCoins = coinAmount,
                            purchaseType = PurchaseType.SELL
                        )
                    } else {
                        Log.d("SellScreen", "Invalid investment amount.")
                    }
                }
            )
        }
    }
}