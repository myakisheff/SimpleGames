package com.example.simplegames.ui.viewModel

import com.example.simplegames.data.game.Game

data class GamesUiState (
    val currentTitle: String = "",
    val currentScores: Int = 0,
    val games: List<Pair<Game, Int>> = listOf()
)