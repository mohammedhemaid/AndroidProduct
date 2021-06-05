package com.app.androidproductstest.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}
