package com.example.simplegames.data.game

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Game(
    val gameID: Long,
    @StringRes val title: Int,
    val pointsForWin: Int,
    @DrawableRes val background: Int
)
