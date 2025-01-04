package com.example.mybitcoinportolioapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.rememberNavController
import com.example.mybitcoinportolioapp.R
import com.example.mybitcoinportolioapp.presentation.navigation.BottomNavItem
import com.example.mybitcoinportolioapp.presentation.navigation.BottomNavigationBar
import com.example.mybitcoinportolioapp.presentation.navigation.Navigation
import com.example.mybitcoinportolioapp.presentation.navigation.TopBar
import com.example.mybitcoinportolioapp.presentation.ui.theme.MyBitcoinPortolioAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBitcoinPortolioAppTheme {
                MyApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopBar(navController = navController)
        },
        bottomBar = {
            BottomNavigationBar (
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = "home",
                        icon = ImageVector.vectorResource(id = R.drawable.home_trend_up)
                    ),
                    BottomNavItem(
                        name = "Buy",
                        route = "buy",
                        icon = ImageVector.vectorResource(id = R.drawable.wallet_add)
                    ),
                    BottomNavItem(
                        name = "Sell",
                        route = "sell",
                        icon = ImageVector.vectorResource(id = R.drawable.empty_wallet)
                    ),
                    BottomNavItem(
                        name = "Settings",
                        route = "settings",
                        icon = ImageVector.vectorResource(id = R.drawable.setting_3)
                    ),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) { innerPadding ->
        Navigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}