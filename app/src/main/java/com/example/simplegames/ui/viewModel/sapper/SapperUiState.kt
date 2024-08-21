package com.example.simplegames.ui.viewModel.sapper

import com.example.simplegames.data.model.SapperCell

/**
 * [currentSizeMode] shows a mode of a current game.
 * [cells] - list of game cells
 * [maxCellsInRow] - maximum of cells in a row to show
 */
data class SapperUiState (
    val currentSizeMode: SapperSizeMode = SapperSizeMode.EASY,
    val currentDifficultMode: SapperDifficultMode = SapperDifficultMode.EASY,
    val cells: List<SapperCell> = listOf(),
    val maxCellsInRow: Int = 6,
    val isSettingsZoneHidden: Boolean = false,
    val isStarted: Boolean = false,
    val currentAward: Int = 0,
    val openedCells: Int = 0,
    val defaultAward: Int = 0,
    val isGameEnd: Boolean = false,
    val isGameWon: Boolean? = null,
    val countOfMines: Int = 0,
    val flaggedCount: Int = 0,
    val isFlaggingMode: Boolean = false
)