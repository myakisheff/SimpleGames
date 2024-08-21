package com.example.simplegames.ui.viewModel.tower

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TowerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TowerUiState())
    val uiState: StateFlow<TowerUiState> = _uiState.asStateFlow()

    fun setAward(award: Int) {
        _uiState.update { state ->
            state.copy(
                defaultAward = award,
                nextAward = calcAward(
                    difficultMode = state.currentDifficultMode,
                    defaultAward = award
                )
            )
        }
    }

    fun cellClick(cellNumber: Int) : Boolean {
        createMines(uiState.value.currentDifficultMode)

        if(!uiState.value.currentMinesPositions[cellNumber]) {
            gameEnd()
            return false
        }

        _uiState.update { state ->
            state.copy(
                currentAward = state.nextAward,
                nextAward = calcAward(state.currentDifficultMode, state.defaultAward),
                gameStarted = true,
                isGameFieldVisible = !state.isGameFieldVisible
            )
        }

        return true
    }

    private fun createMines(difficult: TowerDifficultMode) {
        val mines: MutableList<Boolean> = mutableListOf()

        var cnt = 0
        val positions: MutableList<Int> = mutableListOf()

        while (cnt != difficult.count) {
            val pos = (0..4).random()
            if(!positions.contains(pos)) {
                positions.add(pos)
                cnt++
            }
        }

        repeat(5) {
            if(positions.contains(it))
                mines.add(false)
            else mines.add(true)
        }

        _uiState.update { state ->
            state.copy(
                currentMinesPositions = mines
            )
        }
    }

    private fun gameEnd() {
        _uiState.update { state ->
            state.copy(
                gameStarted = false
            )
        }
    }

    fun hideSettings() {
        _uiState.update { state ->
            state.copy(
                isSettingsHidden = !state.isSettingsHidden
            )
        }
    }

    fun changeDifficult(difficult: TowerDifficultMode) {
        _uiState.update { state ->
            state.copy(
                currentDifficultMode = difficult,
                currentAward = 0,
                nextAward = calcAward(difficultMode = difficult, defaultAward = state.defaultAward),
                gameStarted = false
            )
        }
    }

    private fun calcAward(difficultMode: TowerDifficultMode, defaultAward: Int): Int {
        return uiState.value.currentAward + defaultAward * difficultMode.count
    }
}