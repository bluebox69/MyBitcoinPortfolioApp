package com.example.mybitcoinportolioapp.presentation.homeScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightBlack
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightWhite
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightGrey
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightRed
import java.text.DecimalFormat

@Composable
fun InvestmentCard(
    painter: Painter,
    iconColor: Color,
    coinName: String,
    coinSymbol: String,
    coinMetaInfo: String,
    coinAmount: Double,
    coinValue: Double,
    iconSize: Int,
    modifier: Modifier = Modifier
) {
    val decimalFormat = DecimalFormat("#,##0.00")

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LightBlack)
                .height(80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circle with Bitcoin Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(iconColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "$coinName Logo",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier.size(iconSize.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Coin Name and Symbol Column
                    Column(
                        //modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = coinName,
                            color = LightWhite,
                            fontSize = 18.sp,
                            fontFamily = FontFamilies.fontFamily2,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = coinMetaInfo,
                            color = LightGrey,
                            fontSize = 18.sp,
                            fontFamily = FontFamilies.fontFamily2,
                            fontWeight = FontWeight.ExtraLight
                        )
                    }
                }

                // Coin Value and Amount Column
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$${decimalFormat.format(coinValue)}",
                        color = LightWhite,
                        fontSize = 18.sp,
                        fontFamily = FontFamilies.fontFamily1,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${decimalFormat.format(coinAmount)} $coinSymbol",
                        color = LightGrey,
                        fontSize = 18.sp,
                        fontFamily = FontFamilies.fontFamily1,
                        fontWeight = FontWeight.ExtraLight
                    )
                }
            }
        }
    }
}
