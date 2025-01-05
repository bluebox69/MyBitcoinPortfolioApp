package com.example.mybitcoinportolioapp.presentation.homeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybitcoinportolioapp.R
import com.example.mybitcoinportolioapp.common.toReadableDate
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import com.example.mybitcoinportolioapp.presentation.homeScreen.component.CustomButton
import com.example.mybitcoinportolioapp.presentation.homeScreen.component.InvestmentCard
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightRed
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightYellow
import com.example.mybitcoinportolioapp.presentation.ui.theme.MyGreen
import com.example.mybitcoinportolioapp.presentation.ui.theme.MyRed
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    viewModel: CoinViewModel = koinViewModel() // get ViewModel
) {
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investments = viewModel.investmentsState.value


    val decimalFormat = DecimalFormat("#,##0.00")
    val lastUpdate = portfolioState.lastUpdated

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            IconButton(onClick = { viewModel.getCoin() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_refresh),
                    contentDescription = "Refresh Coin Data",
                    modifier = Modifier.size(34.dp),
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
                text = "$${decimalFormat.format(portfolioState.totalInvestment + portfolioState.totalCash)}",
                fontSize = 45.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            portfolioState.performancePercentage?.let { performance ->
                Text(
                    text = if (performance >= 0) "+${decimalFormat.format(performance)}%" else "${decimalFormat.format(performance)}%",
                    fontSize = 18.sp,
                    fontFamily = FontFamilies.fontFamily2,
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
                fontFamily = FontFamilies.fontFamily2,
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
        ){
            CustomButton(
                buttonText = "Buy",
                buttonColor = LightYellow,
                painter = painterResource(id = R.drawable.icon_buy),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                buttonOnClick = {
                    // Your onClick logic here
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
                    // Your onClick logic here
                }
            )

        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Transactions",
            fontSize = 20.sp,
            fontFamily = FontFamilies.fontFamily2,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
        // Investments List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(investments) {
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
                    coinValue = it.purchasePrice,
                    iconSize = 38,
                    modifier = Modifier
                )
            }
        }
    }
}