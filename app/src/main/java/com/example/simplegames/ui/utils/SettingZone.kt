package com.example.simplegames.ui.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplegames.R
import com.example.simplegames.ui.theme.SimpleGamesTheme
import com.example.simplegames.ui.viewModel.cardGuesser.CardGuesserMode
import com.example.simplegames.ui.viewModel.sapper.SapperDifficultMode
import com.example.simplegames.ui.viewModel.sapper.SapperSizeMode

/**
 * [T] - Used as Enum type of some game modes
 *
 * [currentMode] - Currently chosen mode
 *
 * [onModeClick] - Gets a chosen mode
 *
 * [modes] - A map of all modes and strings to them
 */
@Composable
fun <T> SettingZone(
    currentMode: T,
    onModeClick: (T) -> Unit,
    modes: Map<T, String>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        modes.forEach {
            RadioButton(
                selected = currentMode == it.key,
                onClick = { onModeClick(it.key) }
            )
            Text(text = it.value)
        }
    }
}

@Preview
@Composable
fun DifficultZonePreview() {
    SimpleGamesTheme {
        SettingZone(
            currentMode = CardGuesserMode.EASY,
            onModeClick = {},
            modes = mapOf(
                Pair(CardGuesserMode.EASY, stringResource(id = R.string.game_card_guesser_two_cards)),
                Pair(CardGuesserMode.MEDIUM, stringResource(id = R.string.game_card_guesser_four_cards)),
                Pair(CardGuesserMode.HARD, stringResource(id = R.string.game_card_guesser_six_cards))
            )
        )
    }
}