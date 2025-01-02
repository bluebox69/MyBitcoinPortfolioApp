package com.example.mybitcoinportolioapp.domain.use_case.getCoin

import com.example.mybitcoinportolioapp.common.Resource
import com.example.mybitcoinportolioapp.data.remote.dto.toCoin
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
       try {
           emit(Resource.Loading())
           val coin =  repository.getCoin().toCoin()
           emit(Resource.Success(coin))

       } catch (e: HttpException) {
           emit(Resource.Error(e.localizedMessage ?: "An unexpected Error occurred"))

       } catch (e: IOException) { // If the API cant talk to the remote API or we have no Internet connection
            emit(Resource.Error("Check you internet connection!"))
       }
    }
}