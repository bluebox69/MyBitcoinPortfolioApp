package com.example.mybitcoinportolioapp.presentation.homeScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybitcoinportolioapp.common.Resource
import com.example.mybitcoinportolioapp.domain.model.Coin
import com.example.mybitcoinportolioapp.domain.use_case.getCoin.GetCoinUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CoinViewModel (
    private val getCoinsUseCase: GetCoinUseCase

) : ViewModel() {

    private val _state = mutableStateOf(CoinState())
    val state: State<CoinState> = _state

    /*
    init {
        getCoin()
    }

     */

    fun getCoin() {
        getCoinsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    Log.d("API Request", "Data received: ${result.data}")
                    _state.value = CoinState(coin = result.data ?: Coin("", "", "", 0.0))
                }
                is Resource.Error -> {
                    Log.e("API Request", "Error occurred: ${result.message}")
                    _state.value = CoinState(error = result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Log.d("API Request", "Loading...")
                    _state.value = CoinState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}