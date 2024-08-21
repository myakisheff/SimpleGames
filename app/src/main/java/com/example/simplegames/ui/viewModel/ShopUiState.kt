package com.example.simplegames.ui.viewModel

import com.example.simplegames.data.model.ShopCategory
import com.example.simplegames.data.model.ShopItem

/**
 *
 * @param shopItems `key` present a shop item, `value` present is that item bought
 * @param showOnlyAvailable when `true` shows only items which can be bought
 * @param currentCategory present a selected category, if `null` then no one category selected
 * @param currentPoints current point which uses to buy something
 */
data class ShopUiState (
    val shopItems: Map<ShopItem, Boolean> = mapOf(),
    val showOnlyAvailable: Boolean = false,
    val currentCategory: ShopCategory? = null,
    val currentPoints: Long = 0L,
)