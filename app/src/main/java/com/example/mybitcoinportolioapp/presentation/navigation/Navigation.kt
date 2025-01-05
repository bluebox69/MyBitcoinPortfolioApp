package com.example.mybitcoinportolioapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybitcoinportolioapp.presentation.buyScreen.BuyScreen
import com.example.mybitcoinportolioapp.presentation.homeScreen.HomeScreen
import com.example.mybitcoinportolioapp.presentation.homeScreen.TestHomescreen
import com.example.mybitcoinportolioapp.presentation.sellScreen.SellScreen
import com.example.mybitcoinportolioapp.presentation.settingsScreen.SettingsScreen


@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController = navController) }
        composable("buy") { TestHomescreen() }
        composable("sell") { SellScreen() }
        composable("settings") { SettingsScreen() }
    }

}