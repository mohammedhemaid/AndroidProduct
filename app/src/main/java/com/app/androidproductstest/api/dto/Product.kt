package com.app.androidproductstest.api.dto


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("popularity")
    val popularity: String?,
    @SerializedName("price")
    val price: Double?
)