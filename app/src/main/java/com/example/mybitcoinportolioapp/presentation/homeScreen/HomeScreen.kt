package com.example.mybitcoinportolioapp.presentation.homeScreen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mybitcoinportolioapp.R
import com.example.mybitcoinportolioapp.common.showToast
import com.example.mybitcoinportolioapp.common.toReadableDate
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import com.example.mybitcoinportolioapp.presentation.global_components.CustomButton
import com.example.mybitcoinportolioapp.presentation.homeScreen.component.InvestmentCard
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightBlack
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightRed
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightWhite
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightYellow
import com.example.mybitcoinportolioapp.presentation.ui.theme.MyGreen
import com.example.mybitcoinportolioapp.presentation.ui.theme.MyRed
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    viewModel: CoinViewModel = koinViewModel(),
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investments = viewModel.investmentsState.value


    val decimalFormat = DecimalFormat("#,##0.00")
    val lastUpdate = portfolioState.lastUpdated

    //Toast Message
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()

    //Refresh Rotation
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var rotationTrigger by remember { mutableStateOf(false) }
    var totalRotation by remember { mutableStateOf(0f) }

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
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total Amount",
                    fontSize = 32.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                IconButton(onClick = {
                    totalRotation += 360f
                    rotationTrigger = true
                    viewModel.refreshPortfolio(forceRefresh = true)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_refresh),
                        contentDescription = "Refresh Coin Data",
                        modifier = Modifier
                            .size(34.dp)
                            .graphicsLayer(rotationZ = rotationAngle),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$${decimalFormat.format(portfolioState.currentPortfolioValue + portfolioState.totalCash)}",
                    fontSize = 45.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${decimalFormat.format(portfolioState.profitOrLoss)}",
                        fontSize = 18.sp,
                        fontFamily = FontFamilies.fontFamily1,
                        fontWeight = FontWeight.Light,
                        color = if (portfolioState.profitOrLoss < 0) {
                            MyRed
                        } else {
                            LightBlack
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    portfolioState.performancePercentage?.let { performance ->
                        Text(
                            text = if (performance >= 0) "+${decimalFormat.format(performance)}%" else "${
                                decimalFormat.format(
                                    performance
                                )
                            }%",
                            fontSize = 18.sp,
                            fontFamily = FontFamilies.fontFamily1,
                            fontWeight = FontWeight.Light,
                            color = if (performance > 0) {
                                MyGreen
                            } else if (performance < 0) {
                                MyRed
                            } else {
                                Color.Black
                            }
                        )
                    }
                }
            }
            //Investment
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Investments",
                    fontSize = 20.sp,
                    fontFamily = FontFamilies.fontFamily2,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = "Cash: $${decimalFormat.format(portfolioState.totalCash)}",
                    fontSize = 20.sp,
                    fontFamily = FontFamilies.fontFamily1,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            InvestmentCard(
                painter = painterResource(id = R.drawable.bitcoin_logo),
                coinName = portfolioState.coinName,
                coinSymbol = portfolioState.coinSymbol,
                coinAmount = portfolioState.totalAmount,
                iconColor = LightRed,
                iconSize = 28,
                coinMetaInfo = portfolioState.coinSymbol,
                coinValue = portfolioState.totalInvestment
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomButton(
                    buttonText = "Buy",
                    buttonColor = LightYellow,
                    painter = painterResource(id = R.drawable.icon_buy),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    buttonOnClick = {
                        navController.navigate("buy")
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                CustomButton(
                    buttonText = "Sell",
                    buttonColor = LightRed,
                    painter = painterResource(id = R.drawable.icon_sell),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    buttonOnClick = {
                        navController.navigate("sell")
                    }
                )
            }
            //Transactions
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Transactions",
                fontSize = 20.sp,
                fontFamily = FontFamilies.fontFamily2,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Investments List
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                investments.forEach {
                    InvestmentCard(
                        painter = painterResource(
                            id = if (it.purchaseType == PurchaseType.BUY) R.drawable.icon_buy
                            else R.drawable.icon_sell
                        ),
                        iconColor = if (it.purchaseType == PurchaseType.BUY) LightYellow else LightRed,
                        coinName = it.coinName,
                        coinSymbol = it.coinSymbol,
                        coinMetaInfo = it.date.toReadableDate(),
                        coinAmount = it.quantity,
                        coinValue = it.investmentCost,
                        iconSize = 38,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}