package com.example.simplegames.ui.viewModel.sapper

enum class SapperDifficultMode(val percentOfMines: Int) {
    EASY(percentOfMines = 10),
    MEDIUM(percentOfMines = 35),
    HARD(percentOfMines = 60),
}