package com.app.androidproductstest.api.wrappers

import com.google.gson.annotations.SerializedName

data class BaseResponseWrapper<T>(
    @SerializedName("items")
    val items: T? = null,
    @SerializedName( "message")
    val message: String?,
    @SerializedName( "status")
    val status: Int?,
)