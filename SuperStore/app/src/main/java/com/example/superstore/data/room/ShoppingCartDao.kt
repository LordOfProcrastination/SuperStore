package com.example.superstore.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superstore.data.ShoppingCart
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {

    @Query("SELECT * FROM shoppingcart")
    fun getProductsFromCart() : Flow<List<ShoppingCart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductToCart(items: ShoppingCart)

    @Delete
    suspend fun deleteProductFromCart(item: ShoppingCart)

    @Query("DELETE FROM shoppingcart")
    suspend fun clearShoppingCart()
}