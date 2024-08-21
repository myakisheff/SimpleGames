package com.example.simplegames.data.local

import com.example.simplegames.R
import com.example.simplegames.data.model.ShopCategory
import com.example.simplegames.data.model.ShopItem

object LocalShopItemsDataProvider {
    val allShopItems : List<ShopItem> = listOf(
        ShopItem(
            category = ShopCategory.LANGUAGES,
            title = R.string.languages_russian,
            price = 115L
        ),
    )
}