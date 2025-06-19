package com.digital.order.presentation.screens

import android.R.attr.contentDescription
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.digital.order.domain.DishModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun DishItem(
    dish: DishModel,
    count: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = "",
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(7.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(dish.name)
                Text("${dish.price} ₽")
            }

            Counter(
                count = count,
                onAdd = onAdd,
                onRemove = onRemove,
                enabled = dish.availableQuantity > 0
            )
        }
    }
}