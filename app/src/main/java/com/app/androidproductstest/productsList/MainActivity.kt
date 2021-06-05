package com.app.androidproductstest.productsList

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.app.androidproductstest.R
import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.databinding.ActivityMainBinding
import com.app.androidproductstest.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var productListAdapter: ProductListAdapter
    private var lastFilterSelection = 0

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productListAdapter = ProductListAdapter()
        binding.productList.adapter = productListAdapter

        binding.retry.setOnClickListener {
            productListAdapter.retry()
        }

//        lifecycleScope.launchWhenStarted {
//            viewModel.localProducts.collectLatest { data ->
//                productListAdapter.submitData(data)
//            }
//        }
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            val myItems = listOf(
                getString(R.string.popularity),
                getString(R.string.price),
                getString(R.string.name),
                getString(R.string.brand)
            )

            MaterialDialog(this).show {
                listItemsSingleChoice(
                    items = myItems,
                    initialSelection = lastFilterSelection
                ) { _, index, text ->
                    viewModel.sortItemsBy(text.toString())
                    lastFilterSelection = index
                }
                positiveButton(R.string.filter)
                negativeButton(R.string.cancel)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}