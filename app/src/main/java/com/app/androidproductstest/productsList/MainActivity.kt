package com.app.androidproductstest.productsList

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.databinding.ActivityMainBinding
import com.app.androidproductstest.utils.observe
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var productListAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productListAdapter = ProductListAdapter()
        binding.productList.adapter = productListAdapter

        binding.retry.setOnClickListener {
            productListAdapter.retry()
        }

        observe(viewModel.products, ::setProductList)
    }

    private fun setProductList(pagingData: PagingData<Product>) {
        productListAdapter.submitData(lifecycle, pagingData)
        handleProductListStates()
    }

    private fun handleProductListStates() {
        productListAdapter.addLoadStateListener { loadState ->
            binding.apply {
                retry.isVisible = loadState.source.refresh is LoadState.Error
                youAreOffline.isVisible = loadState.source.refresh is LoadState.Error
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                productList.isVisible = loadState.source.refresh is LoadState.NotLoading

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        productListAdapter.itemCount < 1
                ) {
                    productList.isVisible = false
                }
            }
        }
    }
}