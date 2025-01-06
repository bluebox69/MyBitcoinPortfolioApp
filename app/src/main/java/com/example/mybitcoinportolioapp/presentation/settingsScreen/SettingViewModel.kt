package com.example.mybitcoinportolioapp.presentation.settingsScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybitcoinportolioapp.common.Resource
import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity
import com.example.mybitcoinportolioapp.data.local.entities.purchaseType.PurchaseType
import com.example.mybitcoinportolioapp.data.local.entities.toDomainModel
import com.example.mybitcoinportolioapp.domain.model.Coin
import com.example.mybitcoinportolioapp.domain.use_case.getCoin.GetCoinUseCase
import com.example.mybitcoinportolioapp.domain.use_case.investment.AddInvestmentUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.InitializePortfolioUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.ResetPortfolioUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.UpdatePortfolioUseCase
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.CoinState
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.PortfolioState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingViewModel(
    private val getCoinUseCase: GetCoinUseCase,
    private val resetPortfolioUseCase: ResetPortfolioUseCase,
    private val updatePortfolioUseCase: UpdatePortfolioUseCase,
    private val initializePortfolioUseCase: InitializePortfolioUseCase,

    ) : ViewModel() {

    private val _state = mutableStateOf(CoinState())
    val state: State<CoinState> = _state

    private val _portfolioState = mutableStateOf(PortfolioState())
    val portfolioState: State<PortfolioState> = _portfolioState

    private val _investmentsState = mutableStateOf(emptyList<InvestmentEntity>())
    val investmentsState: State<List<InvestmentEntity>> = _investmentsState

    private val _calculatedDollarAmount = mutableDoubleStateOf(0.0)
    val calculatedDollarAmount: State<Double> = _calculatedDollarAmount

    //Toastmessage State
    private val _toastMessageState = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessageState.asStateFlow()

    //refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var cachedPortfolio: PortfolioState? = null

    init {
        initializePortfolio()
    }

    // Initialize Portfolio
    private fun initializePortfolio() {
        viewModelScope.launch {
            try {
                _portfolioState.value = _portfolioState.value.copy(isLoading = true)
                val portfolio = initializePortfolioUseCase()
                portfolio?.let {
                    _portfolioState.value = PortfolioState(
                        coinName = it.coinName,
                        coinSymbol = it.coinSymbol,
                        totalCash = it.totalCash,
                        totalInvestment = it.totalInvestment,
                        lastUpdated = System.currentTimeMillis(),
                        totalAmount = it.totalAmount
                    )
                }
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to initialize portfolio: ${e.message}"
                )
            }
        }
    }

    private fun setToastMessage(message: String) {
        _toastMessageState.value = message
    }
    fun clearToastMessage() {
        _toastMessageState.value = null
    }

    fun updatePortfolio(dollarAmount: Double) {
        viewModelScope.launch {
            try {
                // Check if portfolio is initialized
                val portfolio = _portfolioState.value
                if (portfolio.isLoading) return@launch

                // Update portfolio values
                val updatedCash = portfolio.totalCash + dollarAmount

                updatePortfolioUseCase(
                    updatedCash,
                    portfolio.totalInvestment,
                    lastUpdated = System.currentTimeMillis(),
                    coinName = portfolio.coinName,
                    coinSymbol = portfolio.coinSymbol,
                    totalAmount = portfolio.totalAmount
                )

                // Reflect updated state
                _portfolioState.value = portfolio.copy(
                    totalCash = updatedCash,
                    lastUpdated = System.currentTimeMillis()
                )

                setToastMessage("Successfully added $${dollarAmount.formatAsCurrency()} to your account!")
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error updating portfolio: ${e.message}")
                setToastMessage("Error updating portfolio: ${e.message}")
            }
        }
    }

    private fun Double.formatAsCurrency(): String {
        return "%.2f".format(this)
    }

    // Reset Portfolio
    fun resetPortfolio() {
        viewModelScope.launch {
            try {
                _portfolioState.value = _portfolioState.value.copy(isLoading = true)
                resetPortfolioUseCase()
                _portfolioState.value = PortfolioState(
                    totalCash = 20000.0,
                    totalInvestment = 0.0,
                    lastUpdated = System.currentTimeMillis(),
                    totalAmount = 0.0,
                    currentPortfolioValue = 0.0,
                    profitOrLoss = 0.0,
                    performancePercentage = 0.0,

                    )
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(error = "Error resetting portfolio: ${e.message}")
            } finally {
                setToastMessage("Reset Successful!")
            }
        }
    }
}