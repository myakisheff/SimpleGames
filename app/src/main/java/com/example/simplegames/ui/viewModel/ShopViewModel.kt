package com.example.simplegames.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.simplegames.data.local.LocalShopItemsDataProvider.allShopItems
import com.example.simplegames.data.model.ShopCategory
import com.example.simplegames.data.model.ShopItem
import com.example.simplegames.ui.AppScreen
import com.example.simplegames.ui.ShopScreenDest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShopViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(
                shopItems = buildMap {
                    allShopItems.forEach {
                        put(it, false)
                    }
                }
            )
        }
    }

    fun selectCategory(category: ShopCategory) {
        _uiState.update { state ->
            state.copy(
                currentCategory = category
            )
        }
    }

    fun setPoints(currentPoints: Long) {
        _uiState.update { state ->
            state.copy(
                currentPoints = currentPoints
            )
        }
    }

    fun setItemToBought(item: ShopItem) {
        val newShopItems = uiState.value.shopItems.toMutableMap()
        newShopItems[item] = true

        _uiState.update { state ->
            state.copy(
                shopItems = newShopItems
            )
        }
    }

    fun getBoughtItems(): List<ShopItem> {
        return uiState.value.shopItems.filter { it.value }.map { it.key }
    }

    fun getShopItemsByCategory(): Map<ShopItem, Boolean> {
        return uiState.value.shopItems.filter {
            it.key.category == uiState.value.currentCategory
        }
    }

    fun getRouteByCategory(): String {
        val category = when(uiState.value.currentCategory) {
            ShopCategory.LANGUAGES -> ShopScreenDest.LANGUAGES
            null -> ShopScreenDest.CATEGORIES
        }
        return category.name
    }
}