package com.example.mybitcoinportolioapp

import android.app.Application
import android.util.Log
import com.example.mybitcoinportolioapp.di.appModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyBitcoinPortfolioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //man startet beim Viewmodel und schaut was injected werden muss und geht dann weiter
        Log.d("KoinDebug", "Koin is being initialized")
        startKoin {
            androidLogger() // standardmäßig, verknüpft den Android Logger mit Koin
            androidContext(this@MyBitcoinPortfolioApplication)// verknüpft den Android Context mit Koin
            modules(appModule)
        }
    }
}