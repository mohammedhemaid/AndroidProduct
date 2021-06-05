package com.app.androidproductstest.productsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.androidproductstest.api.dto.Product
import com.app.androidproductstest.databinding.RowProductBinding
import com.bumptech.glide.Glide

class ProductListAdapter :
    PagingDataAdapter<Product, ProductListAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class ViewHolder(
        private val binding: RowProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(product: Product?) {
            binding.apply {
                Glide.with(image).load(product?.imageUrl).into(image)
                name.text = product?.name
                brand.text = product?.brand
                price.text = product?.price.toString()
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product) =
                oldItem == newItem
        }
    }
}