package com.example.simplegames.ui.gamesScreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplegames.R
import com.example.simplegames.data.model.SapperCell
import com.example.simplegames.ui.theme.SimpleGamesTheme
import com.example.simplegames.ui.utils.SettingZone
import com.example.simplegames.ui.utils.ShowHideSettingsButton
import com.example.simplegames.ui.viewModel.sapper.SapperDifficultMode
import com.example.simplegames.ui.viewModel.sapper.SapperSizeMode
import com.example.simplegames.ui.viewModel.sapper.SapperViewModel


@Composable
fun SapperGameScreen(
    onGameEnd: (Boolean, Int) -> Unit,
    award: Int,
    modifier: Modifier = Modifier
) {
    val viewModel : SapperViewModel = viewModel()

    viewModel.setAward(award)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
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
            if (!uiState.value.isSettingsZoneHidden) {
                SettingZone(
                    currentMode = uiState.value.currentSizeMode,
                    onModeClick = { viewModel.changeSizeMode(it) },
                    modes = buildMap {
                        SapperSizeMode.entries.forEach {
                            put(it, "${it.size * it.size}")
                        }
                    }
                )
                SettingZone(
                    currentMode = uiState.value.currentDifficultMode,
                    onModeClick = { viewModel.changeDifficultMode(it) },
                    modes = buildMap {
                        SapperDifficultMode.entries.forEach {
                            put(it, getDifficultName(it))
                        }
                    }
                )
            }
        }

        ShowHideSettingsButton(
            onClick = { viewModel.hideSettingsZone() },
            isHidden = uiState.value.isSettingsZoneHidden
        )

        InformationTab(
            countOfMines = uiState.value.countOfMines,
            flaggedCount = uiState.value.flaggedCount,
            countOfOpenedCells = uiState.value.openedCells,
            isWon = uiState.value.isGameWon,
            isFlaggingMode = uiState.value.isFlaggingMode,
            isStarted = uiState.value.isStarted,
            onFlaggingModeChange = { viewModel.changeFlaggingMode(it) },
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_medium),
            )
        )

        SapperField(
            cells = uiState.value.cells,
            columns = uiState.value.maxCellsInRow,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            onClick = {
                viewModel.cellClick(it)
                if(uiState.value.isGameEnd) {
                    val isSuccess = uiState.value.isGameWon == true
                    val reward = if(isSuccess) uiState.value.currentAward else 0
                    onGameEnd(isSuccess, reward)
                }
              }
        )
    }
}

@Composable
fun InformationTab(
    countOfMines: Int,
    flaggedCount: Int,
    countOfOpenedCells: Int,
    isWon: Boolean?,
    isFlaggingMode: Boolean,
    isStarted: Boolean,
    onFlaggingModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = !isFlaggingMode,
                onClick = { onFlaggingModeChange(false) }
            )
            Text(text = stringResource(R.string.sapper_standard_mode))
            RadioButton(
                selected = isFlaggingMode,
                onClick = { if(isStarted) onFlaggingModeChange(true) }
            )
            Text(text = stringResource(R.string.sapper_flagging_mode))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.count_of_mines, countOfMines))
            Text(text = stringResource(R.string.sapper_flagged_count, flaggedCount))
            Text(text = stringResource(R.string.sapper_opened, countOfOpenedCells))
            Text(text = stringResource(R.string.status, if (isWon == true) "Won" else if (isWon == false) "Lose" else ""))
        }
    }
}

@Composable
private fun getDifficultName(difficult: SapperDifficultMode) : String {
    return when(difficult) {
        SapperDifficultMode.EASY -> stringResource(id = R.string.difficult_easy)
        SapperDifficultMode.MEDIUM -> stringResource(id = R.string.difficult_medium)
        SapperDifficultMode.HARD -> stringResource(id = R.string.difficult_hard)
    }
}

@Composable
fun SapperField(
    cells: List<SapperCell>,
    columns: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columns),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
    ) {
        items(cells) { cell ->
            Cell(
                cell = cell,
                onClick = { onClick(it) }
            )
        }
    }
}


@Composable
fun Cell(
    cell: SapperCell,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if(cell.isOpened) {
        Card(
            modifier = modifier
                .size(50.dp)
                .border(
                    width = 2.dp,
                    color = if (cell.hasMine) Color.Red else Color.Green,
                    shape = MaterialTheme.shapes.medium
                ),
            onClick = { onClick(cell.id) }
        ) {
            if(cell.hasMine) {
                Image(
                    painter = painterResource(id = R.drawable.sapper_bg),
                    contentDescription = stringResource(R.string.mine),
                    contentScale = ContentScale.Crop
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${if(cell.nearMinesCnt != 0) cell.nearMinesCnt else ""}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    } else {
        Card(
            modifier = modifier
                .size(50.dp)
                .border(
                    width = 2.dp,
                    color = if (cell.isFlagged) Color.Blue else Color.Transparent,
                    shape = MaterialTheme.shapes.medium
                ),
            onClick = { onClick(cell.id) },
        ) {  }
    }
}

@Preview
@Composable
fun SapperGameScreenPreview() {
    SimpleGamesTheme {
        SapperGameScreen(
            onGameEnd = {_, _ ->},
            award = 15,
        )
    }
}

@Preview
@Composable
fun SapperGameScreenDarkThemePreview() {
    SimpleGamesTheme(darkTheme = true) {
        SapperGameScreen(
            onGameEnd = {_, _ ->},
            award = 15,
        )
    }
}

@Preview
@Composable
fun BadCellPreview() {
    SimpleGamesTheme {
        Cell(
            cell = SapperCell(
                id = 0,
                isOpened = true,
                hasMine = true,
                coordinates = Pair(0,0),
                nearMinesCnt = 0,
                isFlagged = false
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun GoodCellPreview() {
    SimpleGamesTheme {
        Cell(
            cell = SapperCell(
                id = 0,
                isOpened = true,
                hasMine = false,
                coordinates = Pair(0,0),
                nearMinesCnt = 0,
                isFlagged = false
            ),
            onClick = {}
        )
    }
}
