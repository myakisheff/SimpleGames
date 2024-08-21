package com.example.simplegames.ui

import androidx.annotation.StringRes
import com.example.simplegames.R

enum class ShopScreenDest(@StringRes val title: Int) {
    CATEGORIES(title = R.string.shop_screen_title),
    LANGUAGES(title = R.string.languages),
}