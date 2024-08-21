package com.example.simplegames.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.simplegames.R
import com.example.simplegames.data.model.Game
import com.example.simplegames.data.local.LocalGamesDataProvide.allGames
import com.example.simplegames.ui.theme.SimpleGamesTheme

@Composable
fun GameListScreen(
    games: Map<Game, Int>,
    pointsTotalCount: Int,
    onGameClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        HeaderGameList(
            pointsCount = pointsTotalCount,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
        GameList(
            games = games,
            onGameClicked = onGameClicked,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxHeight()
        )
    }
}

@Composable
fun HeaderGameList(
    pointsCount: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.available_games),
                style = typography.titleLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.global_points, pointsCount),
                style = typography.titleLarge,
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                color = colorScheme.onPrimary
            )
        }
        Row(modifier = modifier) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.reward_hint),
                style = typography.bodySmall
            )
        }
    }
}

@Composable
fun GameList(
    games: Map<Game, Int>,
    onGameClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        items(games.toList()) { game ->
            GameCard(
                gameNameRes = game.first.title,
                gameImageRes = game.first.background,
                gameTimesPlayed = game.second,
                gameTimesWin = game.first.winTimes,
                gameTimesLose = game.second - game.first.winTimes,
                gameAward = game.first.pointsForWin,
                onClick = { onGameClicked(game.first.gameID) },
                modifier = Modifier.height(dimensionResource(id = R.dimen.game_card_height))
            )
        }
    }
}

@Composable
fun GameCard(
    @StringRes gameNameRes: Int,
    @DrawableRes gameImageRes: Int,
    gameTimesPlayed: Int,
    gameTimesWin: Int,
    gameTimesLose: Int,
    gameAward: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.padding_small)
        ),
        modifier = modifier,

    ) {
        Row {
            DividedImageCard(imageId = gameImageRes)
            GameInfo(
                gameNameRes = gameNameRes,
                gameTimesPlayed = gameTimesPlayed,
                gameTimesWin = gameTimesWin,
                gameTimesLose = gameTimesLose,
                gameAward = gameAward,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        end = dimensionResource(id = R.dimen.padding_medium),
                        bottom = dimensionResource(id = R.dimen.padding_extra)
                    )
            )
        }
    }
}

@Composable
fun DividedImageCard(
    @DrawableRes imageId : Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
        ) {
            val shapeLeft = GenericShape { size: Size, _: LayoutDirection ->
                val width = size.width
                val height = size.height
                moveTo(width / 1.1f, 0f)
                lineTo(width / 1.5f, height / 2f)
                lineTo(width / 1.1f, height)
                lineTo(0f, height)
                lineTo(0f, 0f)
                close()
            }

            val modifierLeft = Modifier
                .size(dimensionResource(id = R.dimen.game_card_height))
                .graphicsLayer {
                    clip = true
                    shape = shapeLeft
                }

            Image(
                modifier = modifierLeft,
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun GameInfo(
    @StringRes gameNameRes: Int,
    gameAward: Int,
    gameTimesPlayed: Int,
    gameTimesWin: Int,
    gameTimesLose: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(
            top = dimensionResource(id = R.dimen.padding_small)
        )){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.game_award, gameAward),
                style = typography.titleMedium
            )
        }
        Text(
            text = stringResource(id = gameNameRes),
            style = typography.titleLarge,
            softWrap = false
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if(gameTimesPlayed != 0) {
                Text(
                    text = stringResource(R.string.played_times, gameTimesPlayed),
                    style = typography.bodySmall
                )
            }

            if(gameTimesWin != 0) {
                Text(
                    text = stringResource(R.string.win_times, gameTimesWin),
                    style = typography.bodySmall,
                    color = colorResource(id = R.color.WinGamesText)
                )
            }

            if(gameTimesLose != 0) {
                Text(
                    text = stringResource(R.string.lose_times, gameTimesLose),
                    style = typography.bodySmall,
                    color = colorResource(id = R.color.LoseGamesText)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamesAppPreview() {
    SimpleGamesTheme {
        GameListScreen(
            games = buildMap {
                allGames.forEach {
                    val playedTimes = (0..17).random()
                    val winTimes = (0..playedTimes).random()
                    val game = it
                    game.winTimes = winTimes
                    put(game, playedTimes)
                }
            },
            onGameClicked = {},
            pointsTotalCount = 115
        )
    }
}

@Preview
@Composable
fun GameCardPreview() {
    SimpleGamesTheme {
        GameCard(
            gameNameRes = R.string.game_sapper_title,
            gameImageRes = R.drawable.sapper_bg,
            gameTimesPlayed = 15,
            gameTimesWin = 0,
            gameTimesLose = 0,
            gameAward = 10,
            onClick = {},
            modifier = Modifier.height(dimensionResource(id = R.dimen.game_card_height))
        )
    }
}

@Preview
@Composable
fun GameCardDarkPreview() {
    SimpleGamesTheme(darkTheme = true) {
        GameCard(
            gameNameRes = R.string.game_sapper_title,
            gameImageRes = R.drawable.sapper_bg,
            gameTimesPlayed = 15,
            gameTimesWin = 0,
            gameTimesLose = 0,
            gameAward = 15,
            onClick = {},
            modifier = Modifier.height(dimensionResource(id = R.dimen.game_card_height))
        )
    }
}