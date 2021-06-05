package com.app.androidproductstest.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.api.services.ProductsService
import com.app.androidproductstest.api.wrappers.BaseResource
import com.app.androidproductstest.productsList.ProductDataSource
import com.app.androidproductstest.api.wrappers.ProcessCallResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productsService: ProductsService) {

    fun getProductListPaging(sort: String, sortDir: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ProductDataSource(this, sort, sortDir) }
    ).liveData

    suspend fun getProductList(
        currentPage: Int,
        perPage: Int,
        sort: String,
        sortDir: String
    ): BaseResource<List<Product>?> {
        return ProcessCallResponse<List<Product>?>().processCall(
            productsService.getProductList(currentPage, perPage, sort, sortDir)
        )
    }
}