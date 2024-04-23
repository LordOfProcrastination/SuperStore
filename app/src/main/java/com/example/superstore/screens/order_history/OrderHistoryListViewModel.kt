package com.example.superstore.screens.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstore.data.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class OrderHistoryListViewModel : ViewModel() {

    val orderHistoryItems = ProductRepository.getAllOrders()
    .stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = emptyList()
    )
}