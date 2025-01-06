package com.example.mybitcoinportolioapp.presentation.buyScreen

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
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.CoinState
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.PortfolioState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BuyViewModel(
    private val getCoinUseCase: GetCoinUseCase,
    private val addInvestmentUseCase: AddInvestmentUseCase,
    private val initializePortfolioUseCase: InitializePortfolioUseCase,

    ) : ViewModel() {

    private val _state = mutableStateOf(CoinState())
    val state: State<CoinState> = _state

    private val _portfolioState = mutableStateOf(PortfolioState())
    val portfolioState: State<PortfolioState> = _portfolioState

    private val _investmentsState = mutableStateOf(emptyList<InvestmentEntity>())
    val investmentsState: State<List<InvestmentEntity>> = _investmentsState

    private val _calculatedBitcoinAmount = mutableDoubleStateOf(0.0)
    val calculatedBitcoinAmount: State<Double> = _calculatedBitcoinAmount

    //Toastmessage State
    private val _toastMessageState = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessageState.asStateFlow()

    //refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        initializePortfolio()
        getCoin()
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

    //Berechnet den anteil an Coins bei einenm Investment
    fun calculateBitcoinAmount(dollarAmount: Double) {
        val currentBitcoinPrice = _state.value.coin.price
        if (currentBitcoinPrice > 0) {
            _calculatedBitcoinAmount.doubleValue = dollarAmount / currentBitcoinPrice
        }
    }

    fun addInvestment(investmentAmountInDollars: Double, purchaseType: PurchaseType) {
        viewModelScope.launch {
            try {
                // Validation ob genügend cash vorhanden ist
                val portfolio = portfolioState.value
                if (portfolio.totalCash < investmentAmountInDollars) {
                    setToastMessage(message = "Not enough cash to make this investment!")
                    return@launch
                }
                // Fetch Coin data
                val latestCoin = getCoinUseCase.invoke().firstOrNull { result ->
                    result is Resource.Success
                } as? Resource.Success<Coin> ?: throw Exception("Failed to fetch latest coin data")

                val updatedCoin = latestCoin.data ?: throw Exception("Coin data is null")

                // Berechnet die Menge an Bitcoins
                val bitcoinAmount = investmentAmountInDollars / updatedCoin.price

                // Investition hinzufügen
                addInvestmentUseCase(
                    coin = updatedCoin,
                    quantity = bitcoinAmount,
                    purchasePrice = updatedCoin.price,
                    purchaseType = purchaseType,
                )
                setToastMessage("Investment added successfully!")
                // Aktualisiert das Portfolio
                refreshPortfolio()
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to add investment: ${e.message}"
                )
            }
        }
    }
    //refresh Portfolio
    fun refreshPortfolio() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _portfolioState.value = _portfolioState.value.copy(isLoading = true)
                // Fetch the latest coin data
                val latestCoin = getCoinUseCase.invoke().firstOrNull { result ->
                    result is Resource.Success
                } as? Resource.Success<Coin> ?: throw Exception("Failed to fetch latest coin data")

                val updatedCoin = latestCoin.data ?: throw Exception("Coin data is null")
                // Refresh Portfolio and Investments
                val portfolio = initializePortfolioUseCase()
                portfolio?.let {
                    _portfolioState.value = PortfolioState(
                        totalCash = it.totalCash,
                        totalInvestment = it.totalInvestment,
                        lastUpdated = System.currentTimeMillis(),
                        totalAmount = it.totalAmount,
                        coinName = it.coinName,
                        coinSymbol = it.coinSymbol
                    )
                }
                getCoin()
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to refresh Portfolio: ${e.message}"
                )
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    // Coin laden (zuerst aus Room, dann aus der API bei Bedarf)
    fun getCoin() {
        viewModelScope.launch {
            try {
                _state.value = CoinState(isLoading = true)

                // Daten aus Room laden
                val localCoin = getCoinUseCase.getCoinFromDatabase()
                if (localCoin != null) {
                    Log.d("CoinViewModel", "Loaded from Room: $localCoin")
                    _state.value = CoinState(coin = localCoin.toDomainModel())
                }

                // Wenn Room leer ist, API-Call durchführen
                getCoinUseCase.invoke().onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            Log.d("API Request", "Data received: ${result.data}")
                            _state.value = CoinState(coin = result.data ?: Coin("", "", "", 0.0))
                        }

                        is Resource.Error -> {
                            Log.e("API Request", "Error occurred: ${result.message}")
                            _state.value =
                                CoinState(error = result.message ?: "An unexpected error occurred")
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