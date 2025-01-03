package com.example.mybitcoinportolioapp.data.local.entities.purchaseType

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromPurchaseType(purchaseType: PurchaseType): String {
        return purchaseType.name
    }

    @TypeConverter
    fun toPurchaseType(value: String): PurchaseType {
        return PurchaseType.valueOf(value)
    }
}
