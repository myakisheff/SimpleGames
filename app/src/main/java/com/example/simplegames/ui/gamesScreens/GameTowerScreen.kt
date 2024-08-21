package com.example.simplegames.ui.gamesScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplegames.R
import com.example.simplegames.ui.theme.SimpleGamesTheme
import com.example.simplegames.ui.utils.SettingZone
import com.example.simplegames.ui.utils.ShowHideSettingsButton
import com.example.simplegames.ui.viewModel.tower.TowerDifficultMode
import com.example.simplegames.ui.viewModel.tower.TowerViewModel

@Composable
fun GameTowerScreen(
    onGameEnd: (Boolean, Int) -> Unit,
    award: Int,
    modifier: Modifier = Modifier
) {
    val viewModel: TowerViewModel = viewModel()
    viewModel.setAward(award)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(id = R.dimen.padding_small)),
    ) {
        val uiState = viewModel.uiState.collectAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            if(!uiState.value.isSettingsHidden) {
                Text(
                    text = stringResource(R.string.tower_count_of_mines),
                    style = MaterialTheme.typography.titleMedium
                )

                SettingZone(
                    currentMode = uiState.value.currentDifficultMode,
                    onModeClick = { viewModel.changeDifficult(it) },
                    modes = buildMap {
                        var i = 0
                        TowerDifficultMode.entries.forEach {
                            i++
                            put(it, "$i")
                        }
                    }
                )
            }
        }

        ShowHideSettingsButton(
            onClick = { viewModel.hideSettings() },
            isHidden = uiState.value.isSettingsHidden
        )

        Spacer(modifier = Modifier.height(100.dp))

        TowerGameZone(
            visible = uiState.value.isGameFieldVisible,
            onClick = {
                val isSuccess = viewModel.cellClick(it)
                if(!isSuccess) {
                    onGameEnd(false, 0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(150.dp))

        AwardInformation(
            currentAward = uiState.value.currentAward,
            nextAward = uiState.value.nextAward,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            enabled = uiState.value.gameStarted,
            onClick = { onGameEnd(true, uiState.value.currentAward) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.tower_collect),
                style = MaterialTheme.typography.displayLarge
            )
        }
    }

}

@Composable
fun TowerGameZone(
    visible: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val enterAnimation = fadeIn(animationSpec = tween(
        delayMillis = 500,
        durationMillis = 500
    )) +
        slideInVertically(animationSpec = tween(
            delayMillis = 500,
            durationMillis = 500,
            easing = LinearOutSlowInEasing
        )) { -it }

    val exitAnimation = slideOutVertically(animationSpec = tween(
        durationMillis = 300,
        easing = LinearOutSlowInEasing
    )) { it / 3 * 2 }

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = !visible,
            enter = enterAnimation,
            exit = exitAnimation,
        ) {
            TowerField(
                color = MaterialTheme.colorScheme.primary,
                onClick = onClick,
                enabled = !visible,
                size = 60.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = enterAnimation,
            exit = exitAnimation,
        ) {
            TowerField(
                color = MaterialTheme.colorScheme.primary,
                onClick = onClick,
                enabled = visible,
                size = 60.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TowerField(
    size: Dp,
    color: Color,
    onClick: (Int) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        repeat(5) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Box(modifier = Modifier
                        .size(size)
                        .border(width = 1.dp, color = color)
                        .clickable(enabled = enabled) {
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AwardInformation(
    nextAward: Int,
    currentAward: Int,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.tower_current_award, currentAward),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.tower_next_award, nextAward),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun TowerFieldPreview() {
    SimpleGamesTheme {
        TowerField(
            color = Color.Black,
            size = 50.dp,
            enabled = true,
            onClick = {}
        )
    }
}


@Preview
@Composable
fun GameTowerScreenPreview() {
    SimpleGamesTheme {
        GameTowerScreen(
            onGameEnd = {_, _ ->},
            award = 15
        )
    }
}

@Preview
@Composable
fun GameTowerScreenDarkPreview() {
    SimpleGamesTheme(darkTheme = true) {
        GameTowerScreen(
            onGameEnd = {_, _ ->},
            award = 15
        )
    }
}