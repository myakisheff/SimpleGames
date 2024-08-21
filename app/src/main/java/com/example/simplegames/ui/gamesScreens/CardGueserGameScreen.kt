package com.example.simplegames.ui.gamesScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplegames.R
import com.example.simplegames.ui.theme.SimpleGamesTheme
import com.example.simplegames.ui.utils.SettingZone
import com.example.simplegames.ui.viewModel.cardGuesser.CardGuesserMode
import com.example.simplegames.ui.viewModel.cardGuesser.CardGuesserViewModel

@Composable
fun CardGuesserGameScreen(
    onGameEnd: (Boolean, Int) -> Unit,
    award: Int,
    modifier: Modifier = Modifier
) {
    val viewModel: CardGuesserViewModel = viewModel()

    viewModel.setAward(award)

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        val uiState = viewModel.uiState.collectAsState()
        viewModel.createCardsIds()

        GameInfo(award = uiState.value.currentAward)

        SettingZone(
            currentMode = uiState.value.currentMode,
            onModeClick = { viewModel.changeMode(it) },
            modes = mapOf(
                Pair(CardGuesserMode.EASY, stringResource(id = R.string.game_card_guesser_two_cards)),
                Pair(CardGuesserMode.MEDIUM, stringResource(id = R.string.game_card_guesser_four_cards)),
                Pair(CardGuesserMode.HARD, stringResource(id = R.string.game_card_guesser_six_cards))
            )
        )
        CardGrid(
            cardIDs = uiState.value.cards.map { it.first },
            onCardClick = {
                val isSuccess = viewModel.checkCardOnWin(it)
                val reward = if(isSuccess) uiState.value.currentAward else 0

                onGameEnd(isSuccess, reward)
                viewModel.endGame()
            }
        )
    }
}

@Composable
fun GameInfo(
    award: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.game_award, award)
        )
    }
}

@Composable
fun CardGrid(
    cardIDs: List<Int>,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        columns = GridCells.Fixed(3),
        modifier = modifier
    ) {
        items(cardIDs) { cardID ->
            CardItem(
                cardID = cardID,
                onClick = { onCardClick(cardID) }
            )
        }
    }
}

@Composable
fun CardItem(
    cardID: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = shapes.large,
        onClick = { onClick(cardID) }
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.cardguesser_card),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = cardID.toString(),
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_medium)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = shapes.small
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = shapes.small
                        )
                        .padding(vertical = 4.dp, horizontal = 10.dp),
                    style = typography.titleLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardGuesserGameScreenPreview() {
    SimpleGamesTheme {
        CardGuesserGameScreen(
            award = 15,
            onGameEnd = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardGuesserGameScreenDarkThemePreview() {
    SimpleGamesTheme(darkTheme = true) {
        CardGuesserGameScreen(
            award = 15,
            onGameEnd = { _, _ -> },
        )
    }
}

@Preview
@Composable
fun CardItemPreview() {
    SimpleGamesTheme {
        CardItem(
            cardID = 0,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun CardItemDarkThemePreview() {
    SimpleGamesTheme(darkTheme = true) {
        CardItem(
            cardID = 0,
            onClick = {}
        )
    }
}