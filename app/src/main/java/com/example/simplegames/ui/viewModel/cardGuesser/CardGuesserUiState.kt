package com.example.simplegames.ui.viewModel.cardGuesser

/**
* [currentMode] shows a mode of a current game.
 * [cards] shows a current list of cards. first - card id, second - is card right
 */
data class CardGuesserUiState(
    val currentMode: CardGuesserMode = CardGuesserMode.EASY,
    val cards: List<Pair<Int, Boolean>> = listOf(),
    val currentAward: Int = 0,
    val defaultAward: Int = 0
)
