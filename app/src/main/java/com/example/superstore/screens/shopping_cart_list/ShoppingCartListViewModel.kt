package com.example.superstore.screens.shopping_cart_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstore.data.OrderHistory
import com.example.superstore.data.ProductRepository
import com.example.superstore.data.ShoppingCart
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class ShoppingCartListViewModel : ViewModel() {

    val shoppingCartItems = ProductRepository.getProductsFromCart()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Removes item from cart if user changes mind
    fun removeFromCart (shoppingCart: ShoppingCart) {
        viewModelScope.launch {
            try {
                ProductRepository.deleteProductFromCart(shoppingCart)
            } catch (e: Exception) {
                Log.e("ShoppingCartListViewModel", "Failed to remove product from cart", e)
            }
        }
    }


    // Calculates the total price of all products
    fun calculateTotalPrice(shoppingCart: List<ShoppingCart>): Double {
        var totalPrice = 0.0

        for (item in shoppingCart) {
            totalPrice += item.price
        }
        return totalPrice
    }

    // Stores all needed information from cart to order history database
    fun orderItems(shoppingCart: List<ShoppingCart>){
        viewModelScope.launch {
            try {
                val orderDate = Calendar.getInstance().time
                val totalAmount = calculateTotalPrice(shoppingCartItems.value)

                val order = OrderHistory(
                    orderDate = orderDate,
                    items = shoppingCart.map { it.title },
                    totalAmount = totalAmount,
                )
                ProductRepository.orderItems(order)
                ProductRepository.clearShoppingCart()
            } catch (e: Exception) {
                Log.e("ShoppingCartListViewModel", "Failed to order items", e)
            }
        }
    }
}