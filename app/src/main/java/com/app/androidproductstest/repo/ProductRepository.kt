package com.app.androidproductstest.repo

import androidx.paging.*
import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.api.services.ProductsService
import com.app.androidproductstest.api.wrappers.BaseResource
import com.app.androidproductstest.api.wrappers.ProcessCallResponse
import com.app.androidproductstest.data.ProductEntity
import com.app.androidproductstest.data.ProductRemoteMediator
import com.app.androidproductstest.data.ProductsDatabase
import com.app.androidproductstest.productsList.ProductDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productsService: ProductsService,
    private val productsDatabase: ProductsDatabase
) {

    private val productsDao = productsDatabase.productDao()

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

    @ExperimentalPagingApi
    fun getProductPaged(
        sort: String,
        sortDir: String,
        refreshOnInit: Boolean
    ): Flow<PagingData<ProductEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 200),
            remoteMediator = ProductRemoteMediator(
                sort,
                sortDir,
                this,
                productsDatabase,
                refreshOnInit
            ),
            pagingSourceFactory = { productsDao.getAllProduct() }
        ).flow
}