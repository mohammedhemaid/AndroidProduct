package com.app.androidproductstest.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM product_entity")
    fun getAllProduct(): PagingSource<Int, ProductEntity>

    @Query("DELETE FROM product_entity")
    fun deleteAllProducts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<ProductEntity>)

}