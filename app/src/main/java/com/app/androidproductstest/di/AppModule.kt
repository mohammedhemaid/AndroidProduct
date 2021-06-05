package com.app.androidproductstest.di

import android.app.Application
import androidx.room.Room
import com.app.androidproductstest.data.ProductsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): ProductsDatabase =
        Room.databaseBuilder(app, ProductsDatabase::class.java, "products_database")
            .fallbackToDestructiveMigration()
            .build()
}