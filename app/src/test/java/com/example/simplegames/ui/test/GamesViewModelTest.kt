package com.example.simplegames.ui.test

import com.example.simplegames.data.local.LocalGamesDataProvide.allGames
import com.example.simplegames.ui.AppScreen
import com.example.simplegames.ui.viewModel.GamesViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GamesViewModelTest {
    private val viewModel = GamesViewModel()

    @Test
    fun gamesViewModel_Init_CorrectGameCountAndPlayedCount() {
        val currentGameUiState = viewModel.uiState.value
        assertEquals(currentGameUiState.games.keys.toList(), allGames)
        assertEquals(currentGameUiState.games.values.sum(), 0)
    }

    @Test
    fun gamesViewModel_GamePick_CorrectUpdatingState() {
        val gameToPick = allGames.random().gameID
        viewModel.pickGame(gameToPick)

        val currentGameUiState = viewModel.uiState.value
        assertEquals(gameToPick, currentGameUiState.currentGameID)
    }

    @Test
    fun gamesViewModel_GameEndSuccess_CorrectUpdatingScoreBeforeFirstPlay() {
        val gameToPick = allGames.random()
        viewModel.pickGame(gameToPick.gameID)
        viewModel.endGame(isSuccess = true)

        val currentGameUiState = viewModel.uiState.value
        assertEquals(gameToPick.pointsForWin, currentGameUiState.currentScores)
    }

    @Test
    fun gamesViewModel_GameEndSuccess_CorrectUpdatingScoreBeforeSecondPlay() {
        var gameToPick = allGames.random()
        viewModel.pickGame(gameToPick.gameID)
        viewModel.endGame(isSuccess = true)

        var rightScore = gameToPick.pointsForWin

        gameToPick = allGames.random()
        viewModel.pickGame(gameToPick.gameID)
        viewModel.endGame(isSuccess = true)

        rightScore += gameToPick.pointsForWin

        val currentGameUiState = viewModel.uiState.value
        assertEquals(rightScore, currentGameUiState.currentScores)
    }

    @Test
    fun gamesViewModel_GameEndLose_CorrectUpdatingScore() {
        val gameToPick = allGames.random()
        viewModel.pickGame(gameToPick.gameID)
        viewModel.endGame(isSuccess = false)

        val currentGameUiState = viewModel.uiState.value
        assertEquals(0, currentGameUiState.currentScores)
    }

    @Test
    fun gamesViewModel_GameRoute_CorrectGameRoute() {
        val gameToFindRoute = allGames.first()
        val route = viewModel.getRouteByGameId(gameToFindRoute.gameID)
        assertEquals(gameToFindRoute.route.name, route)
    }

    @Test
    fun gamesViewModel_GameRouteByBadId_StartMenuRoute() {
        val badGameID = -99L
        val route = viewModel.getRouteByGameId(badGameID)
        assertEquals(AppScreen.START.name, route)
    }
}