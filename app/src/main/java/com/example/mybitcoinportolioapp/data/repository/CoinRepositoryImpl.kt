package com.example.mybitcoinportolioapp.data.repository

import com.example.mybitcoinportolioapp.data.remote.CoinPaprikaAPI
import com.example.mybitcoinportolioapp.data.remote.dto.CoinDto
import com.example.mybitcoinportolioapp.domain.repository.CoinRepository

class CoinRepositoryImpl(
    private val api: CoinPaprikaAPI
) : CoinRepository {
    override suspend fun getCoin(): CoinDto {
        return api.getCoin()
    }
}
