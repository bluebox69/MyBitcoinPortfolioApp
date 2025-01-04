package com.example.mybitcoinportolioapp.presentation.navigation


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mybitcoinportolioapp.R
import com.example.mybitcoinportolioapp.presentation.ui.theme.FontFamilies
import com.example.mybitcoinportolioapp.presentation.ui.theme.IceBlue
import com.example.mybitcoinportolioapp.presentation.ui.theme.LightYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = LightYellow
        ),
        title = {
            Row(verticalAlignment = Alignment.Bottom) {
                if (currentRoute == "home") {
                    Image(
                        painter = painterResource(id = R.drawable.bitcoin_logo),
                        contentDescription = "Bitcoin Logo",
                        colorFilter = ColorFilter.tint(IceBlue),
                        modifier = Modifier
                            .size(45.dp)
                            .alignByBaseline()
                    )
                }
                Text(
                    text = when (currentRoute) {
                        "home" -> "itInvest"
                        "buy" -> "Buy Bitcoin"
                        "sell" -> "Sell Bitcoin"
                        "settings" -> "Settings"
                        else -> "BitInvest"
                    },
                    color = LightYellow,
                    fontSize = 30.sp,
                    fontFamily = FontFamilies.fontFamily2,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        navigationIcon = {
            if (currentRoute != "home") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }
    )
}