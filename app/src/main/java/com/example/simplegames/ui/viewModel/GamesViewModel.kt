package com.example.simplegames.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.simplegames.data.model.Game
import com.example.simplegames.data.local.LocalGamesDataProvide.allGames
import com.example.simplegames.ui.AppScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GamesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(
                games = buildMap {
                    allGames.forEach {
                        put(it, 0)
                    }
                }
            )
        }
    }

    fun pickGame(gameID: Long) {
        _uiState.update { state ->
            state.copy(
                currentGameID = gameID
            )
        }
    }

    fun endGame(isSuccess: Boolean, reward: Int) {
        _uiState.update { state ->
            state.copy(
                currentScores = state.currentScores + reward,
                games = updatedGamesStatus(isSuccess, reward),
                currentGameID = -1,
            )
        }
    }

    fun updateCurrentScreen(currentScreen: AppScreen) {
        _uiState.update { state ->
            state.copy(
                currentScreen = currentScreen
            )
        }
    }

    fun removePoints(points: Long) {
        _uiState.update { state ->
            state.copy(
                currentScores = state.currentScores - points.toInt()
            )
        }
    }

    private fun updatedGamesStatus(isSuccess: Boolean, reward: Int) : Map<Game, Int> {
        val games = uiState.value.games.toMutableMap()

        val key = games.keys.firstOrNull { it.gameID == uiState.value.currentGameID }

        if(key == null) {
            return games
        }

        games[key] = games[key]!!.plus(1)

        games.keys.forEach {
            if(it == key) {
                if(isSuccess) {
                    it.winTimes += 1
                    it.collectedPoints += reward
                }
            }
        }

        return games
    }

    private fun findGameById(id: Long) : Game? {
        return uiState.value.games.keys.firstOrNull { it.gameID == id }
    }

    fun getCurrentGameWinScore() : Int {
        val game = findGameById(uiState.value.currentGameID) ?: return 0
        return game.pointsForWin
    }

    fun getRouteByGameId(gameID: Long): String {
        val game = findGameById(gameID)?.route ?: AppScreen.START
        return game.name
    }

    fun isPlaying(): Boolean {
        return uiState.value.currentGameID != -1L
    }
}