package com.example.simplegames.ui.viewModel.tower

data class TowerUiState(
    val currentDifficultMode: TowerDifficultMode = TowerDifficultMode.EASY,
    val defaultAward: Int = 0,
    val currentAward: Int = 0,
    val nextAward: Int = 0,
    val gameStarted: Boolean = false,
    val isSettingsHidden: Boolean = false,
    val isGameFieldVisible: Boolean = true,
    val currentMinesPositions: List<Boolean> = listOf()
)
