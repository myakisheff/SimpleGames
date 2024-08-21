package com.example.simplegames.ui.viewModel.cardGuesser

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CardGuesserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CardGuesserUiState())
    val uiState: StateFlow<CardGuesserUiState> = _uiState.asStateFlow()

    fun setAward(award: Int) {
        _uiState.update { state ->
            state.copy(
                currentAward = award * state.currentMode.count / 2,
                defaultAward = award
            )
        }
    }

    fun endGame() {
        resetGameState()
    }

    fun createCardsIds() {
        _uiState.update { state ->
            state.copy(
                cards = createCards()
            )
        }
    }

    fun checkCardOnWin(id: Int) : Boolean {
        val card = _uiState.value.cards.firstOrNull { it.first == id }

        if(card == null) {
            return false
        }

        return card.second
    }

    fun changeMode(mode: CardGuesserMode) {
        _uiState.update { state ->
            state.copy(
                currentMode = mode,
                currentAward = state.defaultAward * mode.count / 2
            )
        }
        createCardsIds()
    }

    private fun resetGameState() {
        _uiState.update { state ->
            state.copy(
                currentMode = CardGuesserMode.EASY,
                currentAward = state.defaultAward,
                cards = listOf()
            )
        }
    }

    private fun createCards() : List<Pair<Int, Boolean>> {
        val numOfCards = _uiState.value.currentMode.count
        val winId = (0..<numOfCards).random()

        return buildList {
            repeat(numOfCards) {
                add(Pair(it, it == winId))
            }
        }
    }
}