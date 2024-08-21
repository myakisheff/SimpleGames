package com.example.simplegames.ui.shopScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.simplegames.R
import com.example.simplegames.data.model.ShopItem

@Composable
fun ShopItemsScreen(
    currentPoints: Long,
    onItemBought: (ShopItem) -> Unit,
    shopItems: Map<ShopItem, Boolean>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        ShopHeader(
            currentPoints = currentPoints,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn {
            items(shopItems.toList()) { shopItem ->
                ShopItemCard(
                    shopItem = shopItem.first,
                    onClick = { onItemBought(shopItem.first) },
                    bought = shopItem.second,
                    enabled = currentPoints >= shopItem.first.price && !shopItem.second
                )
            }
        }
    }
}

@Composable
fun ShopHeader(
    currentPoints: Long,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.global_points, currentPoints),
            style = typography.titleLarge,
            modifier = Modifier
                .clip(shapes.medium)
                .background(colorScheme.surfaceTint)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            color = colorScheme.onPrimary
        )
    }
}

@Composable
fun ShopItemCard(
    shopItem: ShopItem,
    bought: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(id = shopItem.title),
                style = typography.displayMedium
            )
            Spacer(modifier = Modifier.weight(1f))

            if(bought) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.item_already_bought)
                )
            } else {
                Text(
                    text = "${shopItem.price}",
                    style = typography.displayMedium
                )
            }
        }
    }
}