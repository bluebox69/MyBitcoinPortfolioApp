package com.example.mybitcoinportolioapp.di

import android.content.Context
import com.example.mybitcoinportolioapp.common.Constant.BASE_URL_COIN
import com.example.mybitcoinportolioapp.data.remote.CoinPaprikaAPI
import com.example.mybitcoinportolioapp.data.repository.CoinRepositoryImpl
import com.example.mybitcoinportolioapp.domain.repository.CoinRepository
import com.example.mybitcoinportolioapp.domain.use_case.getCoin.GetCoinUseCase
import com.example.mybitcoinportolioapp.presentation.homeScreen.CoinViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.room.Room
import com.example.mybitcoinportolioapp.data.local.database.AppDatabase
import com.example.mybitcoinportolioapp.data.local.dao.CoinDao
import com.example.mybitcoinportolioapp.data.local.dao.InvestmentDao
import com.example.mybitcoinportolioapp.data.local.dao.PortfolioDao
import com.example.mybitcoinportolioapp.data.repository.InvestmentRepositoryImpl
import com.example.mybitcoinportolioapp.data.repository.PortfolioRepositoryImpl
import com.example.mybitcoinportolioapp.domain.repository.InvestmentRepository
import com.example.mybitcoinportolioapp.domain.repository.PortfolioRepository
import com.example.mybitcoinportolioapp.domain.use_case.investment.AddInvestmentUseCase
import com.example.mybitcoinportolioapp.domain.use_case.investment.GetInvestmentsUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.InitializePortfolioUseCase
import com.example.mybitcoinportolioapp.domain.use_case.portfolio.UpdatePortfolioUseCase
import java.util.concurrent.TimeUnit


val appModule = module {

    // ViewModel
    viewModelOf(::CoinViewModel)

    // Retrofit API
    single<CoinPaprikaAPI> {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL_COIN)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CoinPaprikaAPI::class.java)
    }

    // Repository
    single<CoinRepository> { CoinRepositoryImpl(get(), get()) }
    single<PortfolioRepository> { PortfolioRepositoryImpl(get()) }
    single<InvestmentRepository> { InvestmentRepositoryImpl(get()) }


    // Use Case
    single { GetCoinUseCase(get()) }
    single { InitializePortfolioUseCase(get()) }
    single { UpdatePortfolioUseCase(get()) }
    single { GetInvestmentsUseCase(get()) }
    single { AddInvestmentUseCase(get(), get()) }

    // Room-Datenbank
    single {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            "coin_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single<CoinDao> {
        get<AppDatabase>().coinDao()
    }
    single<PortfolioDao> {
        get<AppDatabase>().portfolioDao()
    }
    single<InvestmentDao> {
        get<AppDatabase>().investmentDao()
    }
}
