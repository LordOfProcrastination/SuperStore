package com.example.superstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.superstore.data.ProductRepository
import com.example.superstore.screens.order_history.OrderHistoryListScreen
import com.example.superstore.screens.order_history.OrderHistoryListViewModel
import com.example.superstore.screens.product_details.ProductDetailScreen
import com.example.superstore.screens.product_details.ProductDetailsViewModel
import com.example.superstore.screens.product_list.ProductListScreen
import com.example.superstore.screens.product_list.ProductListViewModel
import com.example.superstore.screens.shopping_cart_list.ShoppingCartListScreen
import com.example.superstore.screens.shopping_cart_list.ShoppingCartListViewModel
import com.example.superstore.ui.theme.SuperStoreTheme

class MainActivity : ComponentActivity() {
    // All ViewModels
    private val _productListViewModel: ProductListViewModel by viewModels()
    private val _productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val _shoppingCartListViewModel: ShoppingCartListViewModel by viewModels()
    private val _orderHistoryListViewModel: OrderHistoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProductRepository.initializeDatabase(applicationContext)

        setContent {
            SuperStoreTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "productListScreen"
                    ) {

                    // Routing to ProductList
                    composable(
                        route = "productListScreen"
                    ) {
                        ProductListScreen(
                            viewModel = _productListViewModel,
                            onProductClick = { id ->
                                navController.navigate("productDetailScreen/$id")
                            },
                            onCartClick = {
                                navController.navigate("shoppingCartListScreen")
                            },
                            onOrderHistoryClick = {
                                navController.navigate("orderHistoryListScreen")
                            }
                        )
                    }

                    // Routing to ProductDetails
                    composable(
                        route = "productDetailScreen/{id}",
                        arguments = listOf(
                            navArgument(name = "id") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: -1

                        LaunchedEffect(id){
                            _productDetailsViewModel.setSelectedProduct(id)
                        }

                        ProductDetailScreen(
                            viewModel = _productDetailsViewModel,
                            onBackButtonClick = {
                                navController.popBackStack()
                            },
                            onCartClick = {
                                navController.navigate("shoppingCartListScreen")
                            },
                            onOrderHistoryClick = {
                                navController.navigate("orderHistoryListScreen")
                            }
                        )


                    }

                    // Routing to ShoppingCart
                    composable(
                        route = "shoppingCartListScreen",
                    ) {
                        ShoppingCartListScreen(
                            viewModel = _shoppingCartListViewModel,
                            onBackButtonClick = {
                                navController.popBackStack()
                            },
                            onCartClick = {
                                navController.navigate("shoppingCartListScreen")
                            },
                            onOrderHistoryClick = {
                                navController.navigate("orderHistoryListScreen")
                            }
                        )
                    }

                    // Routing to OrderHistory
                    composable(
                        route = "orderHistoryListScreen",
                    ) {
                        OrderHistoryListScreen(
                            viewModel = _orderHistoryListViewModel,
                            onBackButtonClick = {
                                navController.popBackStack()
                            },
                            onCartClick = {
                                navController.navigate("shoppingCartListScreen")
                            },
                            onOrderHistoryClick = {
                                navController.navigate("orderHistoryListScreen")
                            }
                        )
                    }

                }
            }
        }
    }
}