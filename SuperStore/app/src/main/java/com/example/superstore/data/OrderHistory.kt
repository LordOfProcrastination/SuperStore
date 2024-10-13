package com.example.superstore.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Entity
data class OrderHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @TypeConverters
    val orderDate: Date,
    val items: List<String>,
    val totalAmount: Double
)

class DateConverter {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}