package com.example.superstore.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingCart(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Int,
    val category: String,
    val quantity: Int,
    val imageUrl: String
)