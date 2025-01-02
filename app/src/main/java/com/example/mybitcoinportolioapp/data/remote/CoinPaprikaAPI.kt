package com.example.mybitcoinportolioapp.data.remote

import com.example.mybitcoinportolioapp.data.remote.dto.CoinDto
import retrofit2.http.GET

interface CoinPaprikaAPI {

    @GET("/v1/tickers/btc-bitcoin")
    suspend fun getCoin(): CoinDto

}