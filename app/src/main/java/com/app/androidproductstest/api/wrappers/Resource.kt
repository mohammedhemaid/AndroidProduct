package com.app.androidproductstest.api.wrappers

import retrofit2.Response

typealias GeneralResponse<T> = Response<BaseResponseWrapper<T>>
typealias BaseResource<T> = Resource<BaseResponseWrapper<T>>

// A generic class that contains supplierData and status about loading this supplierData.
sealed class Resource<out T>(
        val data: T? = null,
        val errorCode: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class DataError<T>(errorCode: String) : Resource<T>(null, errorCode)
}