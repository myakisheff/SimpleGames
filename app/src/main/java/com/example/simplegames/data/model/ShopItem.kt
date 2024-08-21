package com.example.simplegames.data.model

import androidx.annotation.StringRes

data class ShopItem (
    val category: ShopCategory,
    @StringRes val title: Int,
    val price: Long,
)