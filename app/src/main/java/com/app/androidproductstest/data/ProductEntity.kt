package com.app.androidproductstest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_entity")

data class ProductEntity(
    val brand: String?,
    @PrimaryKey val id: Int?,
    val imageUrl: String?,
    val name: String?,
    val popularity: String?,
    val price: Double?
)