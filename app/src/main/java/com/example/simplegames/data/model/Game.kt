package com.example.simplegames.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.simplegames.ui.AppScreen

data class Game(
    val gameID: Long,
    @StringRes val title: Int,
    val pointsForWin: Int,
    @DrawableRes val background: Int,
    val route: AppScreen,
    var winTimes: Int = 0,
    var collectedPoints: Long = 0
)
