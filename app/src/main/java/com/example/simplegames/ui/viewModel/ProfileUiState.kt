package com.example.simplegames.ui.viewModel

import com.example.simplegames.R
import com.example.simplegames.data.model.ShopItem

data class ProfileUiState(
    val currentLanguage: Int = 1,
    val currentProfileImage: Any = R.drawable.default_profile,
    val availableItems: List<ShopItem> = listOf(),
)
