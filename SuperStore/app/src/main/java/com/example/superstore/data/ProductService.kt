package com.example.superstore.data

import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    // Gets products from the API
    @GET("products/")
    suspend fun getAllProducts(): Response<List<Product>>

    /* Don't need this anymore after adding ProductDao
    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Response<Product>
     */
}