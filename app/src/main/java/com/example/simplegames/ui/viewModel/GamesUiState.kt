package com.example.simplegames.ui.viewModel

import com.example.simplegames.data.model.Game
import com.example.simplegames.ui.AppScreen

data class GamesUiState (
    val currentScores: Int = 0,
    val currentGameID: Long = -1,
    val games: Map<Game, Int> = mapOf(),
    val gameRoutes: Map<Long, String> = mapOf(),
    val currentScreen: AppScreen = AppScreen.START
)