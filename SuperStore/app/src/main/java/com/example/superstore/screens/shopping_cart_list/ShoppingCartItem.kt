package com.example.superstore.screens.shopping_cart_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.superstore.data.ShoppingCart

@Composable
fun ShoppingCartItem(
    shoppingCart : ShoppingCart,
    onRemoveItemClick: (ShoppingCart) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            )
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(10)
            )
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            model = shoppingCart.imageUrl,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            contentDescription = "Image of ${shoppingCart.title}"
        )
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = shoppingCart.title,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = shoppingCart.category,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "€ ${shoppingCart.price}",
                fontWeight = FontWeight.ExtraBold,
                color = Color.Gray
            )

        }
        IconButton(
            onClick = { onRemoveItemClick(shoppingCart) }
        ) {
            Icon(imageVector = Icons.Default.Clear,
                contentDescription = "Delete Item"
            )
        }
    }
}