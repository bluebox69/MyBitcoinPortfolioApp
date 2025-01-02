package com.example.mybitcoinportolioapp.data.local.entities

import com.example.mybitcoinportolioapp.domain.model.Coin

fun CoinEntity.toDomainModel(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        price = this.price
    )
}
