package com.example.superstore.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.superstore.data.DateConverter
import com.example.superstore.data.OrderHistory
import com.example.superstore.data.Product
import com.example.superstore.data.ProductCategory
import com.example.superstore.data.ShoppingCart
import com.example.superstore.data.StringListConverter

// Here are all my local databases, IF SOMETHING IS ADDED HERE INCREASE VERSION!!
@Database(
    entities = [Product::class, ProductCategory::class, ShoppingCart::class, OrderHistory::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(StringListConverter::class, DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shoppingCartDao(): ShoppingCartDao
    abstract fun orderHistoryDao(): OrderHistoryDao
}