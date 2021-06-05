package com.app.androidproductstest.productsList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.repo.ProductRepository
import retrofit2.HttpException
import java.io.IOException

private const val PAGINATION_STARTING_PAGE = 1

class ProductDataSource(
    private val productRepository: ProductRepository,
    private val sort: String,
    private val sortDir: String
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: PAGINATION_STARTING_PAGE

        return try {
            val response =
                productRepository.getProductList(position, params.loadSize, sort, sortDir)
            val products = response.data?.items ?: ArrayList()

            LoadResult.Page(
                data = products,
                prevKey = if (position == PAGINATION_STARTING_PAGE) null else position - params.loadSize,
                nextKey = if (products.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}