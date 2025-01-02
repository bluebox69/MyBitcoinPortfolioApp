package com.example.mybitcoinportolioapp.domain.use_case.getCoin

import android.util.Log
import com.example.mybitcoinportolioapp.common.Resource
import com.example.mybitcoinportolioapp.data.local.entities.CoinEntity
import com.example.mybitcoinportolioapp.data.local.entities.toDomainModel
import com.example.mybitcoinportolioapp.domain.model.Coin
import com.example.mybitcoinportolioapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetCoinUseCase (
    private val repository: CoinRepository
) {
    // Daten aus Room laden
    suspend fun getCoinFromDatabase(): CoinEntity? {
        Log.d("Room", "Load Data from Database!")
        return repository.getCoinFromDatabase()
    }
    //Daten von API mit Room aktuallisieren
    suspend fun refreshCoinFromApi(): CoinEntity {
        repository.refreshCoinFromApi()
        return repository.getCoinFromDatabase() ?: throw Exception("Failed to refresh data")
    }

    //With Resource we can emit Success , Error oder Loading
   operator fun invoke(): Flow<Resource<Coin>> = flow {
        Log.d("Room", "Load Data from API!")
        try {
           emit(Resource.Loading())
           val coinEntity =  repository.getCoin()
           val coin = coinEntity.toDomainModel() // Konvertiert Entity to Coin
           emit(Resource.Success(coin))

       } catch (e: HttpException) {
           emit(Resource.Error(e.localizedMessage ?: "An unexpected Error occurred"))

       } catch (e: IOException) { // If the API cant talk to the remote API or we have no Internet connection
            emit(Resource.Error("Check you internet connection!"))
       }
    }
}