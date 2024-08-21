package com.example.simplegames.ui

import androidx.annotation.StringRes
import com.example.simplegames.R

enum class AppScreen(@StringRes val title: Int) {
    START(title = R.string.main_screen_title),
    STATISTICS(title = R.string.statistic_screen_title),
    SHOP(title = R.string.shop_screen_title),
    PROFILE(title = R.string.profile_screen_title),
    GAME_SAPPER(title = R.string.game_sapper_title),
    GAME_CARD_GUESSER(title = R.string.game_card_guesser_title),
    GAME_TOWER(title = R.string.game_tower_title)
}