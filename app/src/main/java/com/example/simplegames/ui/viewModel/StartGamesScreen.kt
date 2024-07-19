package com.example.simplegames.ui.viewModel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.simplegames.R
import com.example.simplegames.data.game.Game
import com.example.simplegames.data.local.localGamesDataProvider.allGames
import com.example.simplegames.ui.theme.SimpleGamesTheme

@Composable
fun GameListScreen(
    games: List<Pair<Game, Int>>,
    pointsTotalCount: Int,
    onGameClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        HeaderGameList(pointsCount = pointsTotalCount)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
        GameList(
            games = games,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
fun HeaderGameList(
    pointsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.available_games),
            style = typography.titleLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.global_points, pointsCount),
            style = typography.titleMedium,
            modifier = Modifier
                .clip(shapes.medium)
                .background(colorScheme.surfaceTint)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            color = colorScheme.onPrimary
        )
    }
}

@Composable
fun GameList(
    games: List<Pair<Game, Int>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(games) { game ->
            GameCard(
                gameNameRes = game.first.title,
                gameTimesPlayed = game.second,
                onClick = { /*TODO*/ },
                modifier = Modifier.height(dimensionResource(id = R.dimen.game_card_height))
            )
        }
    }
}

@Composable
fun GameCard(
    @StringRes gameNameRes: Int,
    gameTimesPlayed: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Row {
            DividedImageCard(imageId = R.drawable.sapper_bg)
            GameInfo(
                gameNameRes = gameNameRes,
                gameTimesPlayed = gameTimesPlayed,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        end = dimensionResource(id = R.dimen.padding_medium),
                        bottom = dimensionResource(id = R.dimen.padding_medium)
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
    gameTimesPlayed: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = gameNameRes),
            style = typography.displaySmall,
            softWrap = false
        )
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.played_times, gameTimesPlayed),
                style = typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun GamesAppPreview() {
    SimpleGamesTheme {
        GameListScreen(
            games = buildList {
                allGames.forEach {
                    add(Pair(it, (0..17).random()))
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
            gameTimesPlayed = 15,
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
            gameTimesPlayed = 15,
            onClick = {},
            modifier = Modifier.height(dimensionResource(id = R.dimen.game_card_height))
        )
    }
}