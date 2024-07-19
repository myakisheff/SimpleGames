package com.example.simplegames.ui

import androidx.annotation.StringRes
import com.example.simplegames.R

enum class GameScreen(@StringRes val title: Int) {
    START(title = R.string.game_sapper_title),
    GAME_SAPPER(title = R.string.game_sapper_title)
}