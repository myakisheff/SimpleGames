package com.example.simplegames.data.local

import com.example.simplegames.R
import com.example.simplegames.data.model.Game
import com.example.simplegames.ui.AppScreen

object LocalGamesDataProvide {
    val allGames : List<Game> = listOf(
        Game(
            gameID = 0,
            title = R.string.game_sapper_title,
            pointsForWin = 20,
            background = R.drawable.sapper_bg,
            route = AppScreen.GAME_SAPPER
        ),
        Game(
            gameID = 1,
            title = R.string.game_card_guesser_title,
            pointsForWin = 15,
            background = R.drawable.card_guesser_bg,
            route = AppScreen.GAME_CARD_GUESSER
        ),
        Game(
            gameID = 2,
            title = R.string.game_tower_title,
            pointsForWin = 10,
            background = R.drawable.tower,
            route = AppScreen.GAME_TOWER
        ),
    )
}