package com.example.mybitcoinportolioapp.domain.repository

import com.example.mybitcoinportolioapp.data.remote.dto.CoinDto

interface CoinRepository {

    suspend fun getCoin(): CoinDto

}