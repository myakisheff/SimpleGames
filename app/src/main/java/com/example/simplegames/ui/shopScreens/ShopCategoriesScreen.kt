package com.example.simplegames.ui.shopScreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.simplegames.R
import com.example.simplegames.data.model.ShopCategory
import com.example.simplegames.ui.CategoryTile

@Composable
fun ShopCategoriesScreen(
    onCategoryClick: (ShopCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(ShopCategory.entries) { category ->
            CategoryTile(
                titleRes = when(category) {
                    ShopCategory.LANGUAGES -> R.string.languages
                },
                onCategoryClick = { onCategoryClick(category) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}