package com.example.mybitcoinportolioapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybitcoinportolioapp.presentation.buyScreen.BuyScreen
import com.example.mybitcoinportolioapp.presentation.homeScreen.HomeScreen
import com.example.mybitcoinportolioapp.presentation.sellScreen.SellScreen
import com.example.mybitcoinportolioapp.presentation.settingsScreen.SettingsScreen


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen() }
        composable("buy") { BuyScreen() }
        composable("sell") { SellScreen() }
        composable("settings") { SettingsScreen() }
    }

}