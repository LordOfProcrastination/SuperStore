package com.example.superstore.screens.product_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstore.data.Product
import com.example.superstore.data.ProductRepository
import com.example.superstore.data.ShoppingCart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {

    var selectedProduct: StateFlow<Product?> = MutableStateFlow(null)
        private set

    fun setSelectedProduct(id: Int) {
        selectedProduct = ProductRepository.getProductById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = null
            )
    }

    // Uses the selected product information and stores it in the cart database
    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                val shoppingCartItem = ShoppingCart(
                    id = product.id,
                    title = product.title,
                    price = product.price,
                    category = product.category.name,
                    quantity = 1,
                    imageUrl = product.imageUrls.firstOrNull() ?: ""
                )
                ProductRepository.insertProductToCart(shoppingCartItem)
            } catch (e: Exception) {
                Log.e("ProductDetailsViewModel", "Failed to add product to cart", e)
            }
        }
    }
}