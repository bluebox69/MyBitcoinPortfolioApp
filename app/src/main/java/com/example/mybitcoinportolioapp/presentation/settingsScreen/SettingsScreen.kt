package com.example.mybitcoinportolioapp.presentation.settingsScreen


import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybitcoinportolioapp.R
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightWhite
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.mybitcoinportolioapp.common.showToast
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import com.example.mybitcoinportolioapp.presentation.global_components.CustomButton
import com.example.mybitcoinportolioapp.presentation.sellScreen.SellViewModel
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightRed
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightYellow


@Composable
fun SettingsScreen(
    viewModel: SettingViewModel = koinViewModel()
) {
    val coinState = viewModel.state.value
    val portfolioState = viewModel.portfolioState.value
    val investmentState = viewModel.investmentsState.value

    val scrollState = rememberScrollState()
    val decimalFormat = DecimalFormat("#,##0.00")
    val decimalFormat2 = DecimalFormat("#,##0.00000")
    val lastUpdate = portfolioState.lastUpdated

    var investAmount by remember { mutableStateOf("") }

    //Toast Message
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()

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
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = investAmount,
                onValueChange = { newText ->
                    // Validieren
                    if (newText.all { it.isDigit() || it == '.' }) {
                        investAmount = newText
                    }
                },
                label = { Text("Add Money To Account") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                buttonText = "Deposit",
                buttonColor = LightYellow,
                painter = painterResource(id = R.drawable.wallet_add),
                modifier = Modifier
                    .fillMaxWidth(),
                buttonOnClick = {
                    val dollarAmount = investAmount.toDoubleOrNull() ?: 0.0
                    if (dollarAmount > 0) {
                        viewModel.updatePortfolio(dollarAmount)
                    } else {
                        Log.d("SettingsScreen", "Invalid Dollar amount.")
                    }
                }
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Reset All Data And Delete All Investments",
                fontSize = 14.sp,
                fontFamily = FontFamilies.fontFamily1,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton(
                buttonText = "Reset Portfolio",
                buttonColor = LightRed,
                painter = painterResource(id = R.drawable.arrows_vertical),
                modifier = Modifier
                    .fillMaxWidth(),
                buttonOnClick = {
                    viewModel.resetPortfolio()
                }
            )
        }
    }
}