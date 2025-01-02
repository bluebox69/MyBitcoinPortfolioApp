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
    //With Resource we can emit Success , Error oder Loading
   operator fun invoke(): Flow<Resource<Coin>> = flow {
        Log.d("Room", "Load Data from API!")
        try {
           emit(Resource.Loading())
           val coin =  repository.getCoin()
           emit(Resource.Success(coin))

       } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                404 -> "Coin not found. Please check the API request."
                500 -> "Server error. Please try again later."
                else -> "An unexpected error occurred: ${e.message()}"
            }
            emit(Resource.Error(errorMessage))

       } catch (e: IOException) { // If the API cant talk to the remote API or we have no Internet connection
            emit(Resource.Error("Check you internet connection!"))
       }
    }

    // Daten aus Room laden
    suspend fun getCoinFromDatabase(): CoinEntity? {
        Log.d("Room", "Load Data from Database!")
        return repository.getCoinFromDatabase()
    }
}