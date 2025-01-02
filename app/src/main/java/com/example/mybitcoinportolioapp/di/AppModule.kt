package com.example.mybitcoinportolioapp.di

import com.example.mybitcoinportolioapp.common.Constant.BASE_URL_COIN
import com.example.mybitcoinportolioapp.data.remote.CoinPaprikaAPI
import com.example.mybitcoinportolioapp.data.repository.CoinRepositoryImpl
import com.example.mybitcoinportolioapp.domain.repository.CoinRepository
import com.example.mybitcoinportolioapp.domain.use_case.getCoin.GetCoinUseCase
import com.example.mybitcoinportolioapp.presentation.homeScreen.CoinViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // ViewModel
    viewModelOf(::CoinViewModel)

    // Retrofit API
    single<CoinPaprikaAPI> {
        Retrofit.Builder()
            .baseUrl(BASE_URL_COIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaAPI::class.java)
    }

    // Repository
    single<CoinRepository> { CoinRepositoryImpl(get()) }

    // Use Case
    single { GetCoinUseCase(get()) }
}
