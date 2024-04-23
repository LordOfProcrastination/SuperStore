package com.example.superstore.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Int,
    val description: String,
    @Embedded(prefix = "category_")
    val category: ProductCategory,
    @TypeConverters(StringListConverter::class)
    @SerializedName("images")
    val imageUrls: List<String>,
)
@Entity
data class ProductCategory(
    @PrimaryKey val id: Int,
    val name: String,
    @SerializedName("image")
    val categoryImageUrl: String,
)

class StringListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun toStringList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }
}