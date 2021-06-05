package com.app.androidproductstest.productsList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.app.androidproductstest.base.BaseViewModel
import com.app.androidproductstest.repo.ProductRepository

class MainActivityViewModel @ViewModelInject constructor(
    private val productRepository: ProductRepository,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val products = currentQuery.switchMap { query ->
        productRepository.getProductListPaging(
            sort = query,
            sortDir = "asc"
        ).cachedIn(viewModelScope)
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "popularity"
    }
}