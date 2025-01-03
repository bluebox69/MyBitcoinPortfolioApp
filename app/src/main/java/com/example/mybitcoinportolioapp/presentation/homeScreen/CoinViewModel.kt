package com.example.mybitcoinportolioapp.presentation.homeScreen

import android.util.Log
import androidx.compose.runtime.State
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
import com.example.mybitcoinportolioapp.domain.use_case.investment.GetInvestmentsUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.InitializePortfolioUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.ResetPortfolioUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.UpdatePortfolioUseCase
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.CoinState
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.PortfolioState
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CoinViewModel (
    private val getCoinsUseCase: GetCoinUseCase,
    private val initializePortfolioUseCase: InitializePortfolioUseCase,
    private val getInvestmentsUseCase: GetInvestmentsUseCase,
    private val addInvestmentUseCase: AddInvestmentUseCase,
    private val updatePortfolioUseCase: UpdatePortfolioUseCase,
    private val resetPortfolioUseCase: ResetPortfolioUseCase


    ) : ViewModel() {

    private val _state = mutableStateOf(CoinState())
    val state: State<CoinState> = _state

    private val _portfolioState = mutableStateOf(PortfolioState())
    val portfolioState: State<PortfolioState> = _portfolioState

    private val _investmentsState = mutableStateOf(emptyList<InvestmentEntity>())
    val investmentsState: State<List<InvestmentEntity>> = _investmentsState

    init {
        loadCoinFromDatabase()
        initializePortfolio()
        loadInvestments()
    }

    // Room-Daten laden
    private fun loadCoinFromDatabase() {
        viewModelScope.launch {
            val localCoin = getCoinsUseCase.getCoinFromDatabase()
            if (localCoin != null) {
                _state.value = CoinState(coin = localCoin.toDomainModel())
            }
        }
    }
    // Initialize Portfolio
    private fun initializePortfolio() {
        viewModelScope.launch {
            try {
                _portfolioState.value = _portfolioState.value.copy(isLoading = true)
                val portfolio = initializePortfolioUseCase()
                portfolio?.let {
                    _portfolioState.value = PortfolioState(
                        totalCash = it.totalCash,
                        totalInvestment = it.totalInvestment,
                        lastUpdated = it.lastUpdated
                    )
                }
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to initialize portfolio: ${e.message}"
                )
            }
        }
    }

    // Load Investments
    private fun loadInvestments() {
        viewModelScope.launch {
            try {
                val investments = getInvestmentsUseCase()
                _investmentsState.value = investments
            } catch (e: Exception) {
                Log.e("ViewModel", "Cant load Investments")
            }
        }
    }

    // Add Investment
    fun addInvestment(coin: Coin, quantity: Double, purchasePrice: Double, purchaseType: PurchaseType) {
        viewModelScope.launch {
            try {
                _portfolioState.value = _portfolioState.value.copy(isLoading = true)

                // Fetch the latest coin data
                val latestCoin = getCoinsUseCase.invoke().firstOrNull { result ->
                    result is Resource.Success
                } as? Resource.Success<Coin> ?: throw Exception("Failed to fetch latest coin data")

                val updatedCoin = latestCoin.data ?: throw Exception("Coin data is null")

                addInvestmentUseCase(
                    coin = updatedCoin,
                    quantity = quantity,
                    purchasePrice = updatedCoin.price,
                    purchaseType = purchaseType
                )

                // Refresh Portfolio and Investments
                val portfolio = initializePortfolioUseCase()
                portfolio?.let {
                    _portfolioState.value = PortfolioState(
                        totalCash = it.totalCash,
                        totalInvestment = it.totalInvestment,
                        lastUpdated = it.lastUpdated
                    )
                }
                loadInvestments()
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to add investment: ${e.message}"
                )
            }
        }
    }
    // Update Portfolio (if needed)
    fun updatePortfolio(totalCash: Double, totalInvestment: Double, lastUpdated: Long) {
        viewModelScope.launch {
            try {
                updatePortfolioUseCase(totalCash, totalInvestment, lastUpdated)
                _portfolioState.value = PortfolioState(
                    totalCash = totalCash,
                    totalInvestment = totalInvestment,
                    lastUpdated = lastUpdated
                )
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to update portfolio: ${e.message}"
                )
            }
        }
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
                    lastUpdated = System.currentTimeMillis()
                )
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(error = "Error resetting portfolio: ${e.message}")
            }
        }
    }


    // Coin laden (zuerst aus Room, dann aus der API bei Bedarf)
    fun getCoin() {
        viewModelScope.launch {
            try {
                _state.value = CoinState(isLoading = true)

                // Daten aus Room laden
                val localCoin = getCoinsUseCase.getCoinFromDatabase()
                if (localCoin != null) {
                    Log.d("CoinViewModel", "Loaded from Room: $localCoin")
                    _state.value = CoinState(coin = localCoin.toDomainModel())
                }

                // Wenn Room leer ist, API-Call durchführen
                getCoinsUseCase.invoke().onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            Log.d("API Request", "Data received: ${result.data}")
                            _state.value = CoinState(coin = result.data ?: Coin("", "", "", 0.0))
                        }
                        is Resource.Error -> {
                            Log.e("API Request", "Error occurred: ${result.message}")
                            _state.value = CoinState(error = result.message ?: "An unexpected error occurred")
                        }
                        is Resource.Loading -> {
                            Log.d("API Request", "Loading from API...")
                            _state.value = CoinState(isLoading = true)
                        }
                    }
                }.launchIn(viewModelScope)
            } catch (e: Exception) {
                Log.e("CoinViewModel", "Error: ${e.message}")
                _state.value = CoinState(error = "Error: ${e.message}")
            }
        }
    }
    
}