package com.app.androidproductstest.api.services

import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.api.wrappers.GeneralResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsService {

    @GET("products")
    suspend fun getProductList(
        @Query("page") currentPage: Int,
        @Query("maxitems") perPage: Int,
        @Query("sort") sort: String,
        @Query("dir") sortDir: String
    ): GeneralResponse<List<Product>>
}