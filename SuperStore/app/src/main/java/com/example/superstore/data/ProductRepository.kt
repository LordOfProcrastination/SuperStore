package com.example.superstore.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.superstore.data.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {

    // Httpclient
    private val _httpClient : OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    // Retrofit
    private val _retrofit : Retrofit = Retrofit.Builder()
        .client(_httpClient)
        .baseUrl("https://api.escuelajs.co/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // To make API calls from the service
    private val _productService = _retrofit.create(ProductService::class.java)

    private lateinit var _appDatabase: AppDatabase
    // To make queries in the databases
    private val _productDao by lazy { _appDatabase.productDao() }
    private val _shoppingCartDao by lazy { _appDatabase.shoppingCartDao() }
    private val _orderHistoryDao by lazy { _appDatabase.orderHistoryDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).fallbackToDestructiveMigration().build()
    }
    /*
     Gets all products from the API and stores them in a local database for products,
     to reduce loading times and adding offline functionality
     */
    fun getProducts(): Flow<List<Product>> = flow {
        try {
            val response = _productService.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    _productDao.insertProducts(products)
                    emitAll(_productDao.getProducts())
                } ?: emit(emptyList())
            } else {
                throw Exception("Response not successful")
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to get list of products", e)
            emit(emptyList())
        }
    }.onStart {
        emit(_productDao.getProducts().first())
    }.catch {e ->
        Log.e("ProductRepository", "Failed to get list of products", e)
    }.flowOn(Dispatchers.IO)

    // Uses the local database to get ID to show a singe product
    fun getProductById(id: Int): Flow<Product?> {
        return _productDao.getProductById(id).flowOn(Dispatchers.IO)
    }

    // Inserts selected product into a local database for cart
    suspend fun insertProductToCart(item: ShoppingCart) {
        try {
            _shoppingCartDao.insertProductToCart(item)
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to insert product to cart", e)
        }
    }

    // Deletes an item from the cart
    suspend fun deleteProductFromCart(item: ShoppingCart) {
        try {
            _shoppingCartDao.deleteProductFromCart(item)
        } catch (e: Exception) {
            (Log.e("ProductRepository", "Failed to delete product from cart", e))
        }
    }

    // Shows all products in the cart database
    fun getProductsFromCart() : Flow<List<ShoppingCart>> {
        return _shoppingCartDao.getProductsFromCart()
            .catch {e ->
                Log.e("ProductRepository", "Failed to get products from cart", e)
                emit(emptyList())
            }.flowOn(Dispatchers.IO)
    }

    // Clears all items after making order
    suspend fun clearShoppingCart() {
        try {
            _shoppingCartDao.clearShoppingCart()
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to clear shopping cart", e)
        }
    }

    // Gets all order history from local database
    fun getAllOrders(): Flow<List<OrderHistory>> {
        return _orderHistoryDao.getAllOrders()
            .catch {e ->
                Log.e("ProductRepository", "Failed to get order history", e)
                emit(emptyList())
            }.flowOn(Dispatchers.IO)
    }

    // Transfers products from shopping cart to order history
    suspend fun orderItems(item: OrderHistory) {
        try {
            _orderHistoryDao.orderItems(item)
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to order items", e)
        }
    }


}