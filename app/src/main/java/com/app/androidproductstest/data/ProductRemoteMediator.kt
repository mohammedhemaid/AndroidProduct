package com.app.androidproductstest.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.androidproductstest.repo.ProductRepository
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class ProductRemoteMediator(
    private val sort: String,
    private val sortDir: String,
    private val repo: ProductRepository,
    private val productsDatabase: ProductsDatabase,
    private val refreshOnInit: Boolean
) : RemoteMediator<Int, ProductEntity>() {

    private val productsDao = productsDatabase.productDao()

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> 1
        }

        try {
            val serverProducts = repo.getProductList(page, state.config.pageSize, sort, sortDir)

            val products = serverProducts.data?.items?.map { serverProduct ->
                ProductEntity(
                    id = serverProduct.id,
                    name = serverProduct.name,
                    brand = serverProduct.brand,
                    imageUrl = serverProduct.imageUrl,
                    popularity = serverProduct.popularity,
                    price = serverProduct.price,
                )
            } ?: ArrayList()

            productsDatabase.withTransaction {
                productsDao.deleteAllProducts()
                productsDao.insertProducts(products)
            }
            return MediatorResult.Success(
                endOfPaginationReached = serverProducts.data?.items?.isEmpty() ?: true
            )
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    @ExperimentalPagingApi
    override suspend fun initialize(): InitializeAction {
        return if (refreshOnInit) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }
}