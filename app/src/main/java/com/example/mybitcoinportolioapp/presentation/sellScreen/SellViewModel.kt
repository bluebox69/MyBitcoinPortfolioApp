package com.example.mybitcoinportolioapp.presentation.sellScreen

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
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.UpdatePortfolioUseCase
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.CoinState
import com.example.mybitcoinportolioapp.presentation.homeScreen.state.PortfolioState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SellViewModel(
    private val getCoinUseCase: GetCoinUseCase,
    private val addInvestmentUseCase: AddInvestmentUseCase,
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
    fun calculateBitcoinValue(coinAmount: Double) {
        val currentBitcoinPrice = _state.value.coin.price
        if (currentBitcoinPrice > 0) {
            _calculatedDollarAmount.doubleValue = coinAmount * currentBitcoinPrice
        }
    }

    fun processSellOrder(sellingAmountInCoins: Double, purchaseType: PurchaseType) {
        viewModelScope.launch {
            try {
                // Validation ob genügend Coins vorhanden sind
                val portfolio = portfolioState.value
                if (portfolio.totalAmount < sellingAmountInCoins) {
                    setToastMessage(message = "Not enough Coins to sell!")
                    return@launch
                }
                // Fetch Coin data
                val latestCoin = getCoinUseCase.invoke().firstOrNull { result ->
                    result is Resource.Success
                } as? Resource.Success<Coin> ?: throw Exception("Failed to fetch latest coin data")

                val updatedCoin = latestCoin.data ?: throw Exception("Coin data is null")

                // Berechnet die Menge an Bitcoins
                val dollarAmount = sellingAmountInCoins * updatedCoin.price

                // Investition hinzufügen
                addInvestmentUseCase(
                    coin = updatedCoin,
                    quantity = sellingAmountInCoins,
                    purchasePrice = updatedCoin.price,
                    purchaseType = purchaseType,
                )

                // Update portfolio totalCash and totalInvestment
                val newTotalCash = portfolio.totalCash + dollarAmount
                val newTotalInvestment = portfolio.totalInvestment - sellingAmountInCoins

                // Update portfolio
                refreshPortfolio(forceRefresh = true)
                setToastMessage("Sell order processed successfully!")
            } catch (e: Exception) {
                _portfolioState.value = PortfolioState(
                    error = "Failed to sell investment: ${e.message}"
                )
            }
        }
    }
    //refresh Portfolio
    fun refreshPortfolio(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                if (cachedPortfolio != null && !forceRefresh) {
                    _portfolioState.value = cachedPortfolio!!
                    return@launch
                }

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
                    Log.d("SellViewModel", "Loaded from Room: $localCoin")
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
                Log.e("SellViewModel", "Error: ${e.message}")
                _state.value = CoinState(error = "Error: ${e.message}")
            }
        }
    }
}