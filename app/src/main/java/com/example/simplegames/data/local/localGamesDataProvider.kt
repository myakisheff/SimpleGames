package com.example.simplegames.data.local

import com.example.simplegames.R
import com.example.simplegames.data.game.Game

object localGamesDataProvider {
    val allGames : List<Game> = listOf(
        Game(
            gameID = 0,
            title = R.string.game_sapper_title,
            pointsForWin = 10,
            background = R.drawable.sapper_bg
        ),
    )
}