package com.example.simplegames.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplegames.R
import com.example.simplegames.data.local.LocalGamesDataProvide.allGames
import com.example.simplegames.data.model.Game
import com.example.simplegames.ui.theme.SimpleGamesTheme

@Composable
fun StatisticsScreen(
    games: Map<Game, Int>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(games.toList()) { game ->
            GameStat(
                gameNameRes = game.first.title,
                gameTimesPlayed = game.second,
                gameTimesWin = game.first.winTimes,
                gameTimesLose = game.second - game.first.winTimes,
                collectedPoints = game.first.collectedPoints,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun GameStat(
    gameNameRes: Int,
    gameTimesPlayed: Int,
    gameTimesWin: Int,
    gameTimesLose: Int,
    collectedPoints: Long,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = gameNameRes),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.played_times, gameTimesPlayed))
                Text(text = stringResource(id = R.string.win_times, gameTimesWin))
                Text(text = stringResource(id = R.string.lose_times, gameTimesLose))
            }
            Text(
                text = stringResource(R.string.collected_points, collectedPoints),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun StatisticsScreenPreview() {
    SimpleGamesTheme {
        StatisticsScreen(
            games = buildMap {
                allGames.forEach {
                    val playedTimes = (0..17).random()
                    val winTimes = (0..playedTimes).random()
                    val game = it
                    game.winTimes = winTimes
                    put(game, playedTimes)
                }
            },
        )
    }
}
