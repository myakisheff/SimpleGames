package com.example.simplegames.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.simplegames.R
import com.example.simplegames.ui.gamesScreens.CardGuesserGameScreen
import com.example.simplegames.ui.gamesScreens.GameTowerScreen
import com.example.simplegames.ui.gamesScreens.SapperGameScreen
import com.example.simplegames.ui.theme.SimpleGamesTheme
import com.example.simplegames.ui.utils.NavigationItemContent
import com.example.simplegames.ui.viewModel.GamesViewModel
import com.example.simplegames.ui.viewModel.ProfileViewModel
import com.example.simplegames.ui.viewModel.ShopViewModel

@Composable
fun GamesApp() {
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.START.name
    )

    val navigationItemContentList = listOf(
        NavigationItemContent(
            screen = AppScreen.START,
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.main_screen_title)
        ),
        NavigationItemContent(
            screen = AppScreen.STATISTICS,
            icon = Icons.Default.Star,
            text = stringResource(id = R.string.statistic_screen_title)
        ),
        NavigationItemContent(
            screen = AppScreen.SHOP,
            icon = Icons.Default.ShoppingCart,
            text = stringResource(id = R.string.shop_screen_title)
        ),
        NavigationItemContent(
            screen = AppScreen.PROFILE,
            icon = Icons.Default.Person,
            text = stringResource(id = R.string.profile_screen_title)
        )
    )

    val viewModel: GamesViewModel = viewModel()
    val shopViewModel: ShopViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = viewModel.isPlaying(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GamesTopAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
                        navController.navigateUp()
                        viewModel.endGame(isSuccess = false, reward = 0)
                    }
                )
            }

        },
        bottomBar = {
            AnimatedVisibility(
                visible = !viewModel.isPlaying(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BottomNavigationBar(
                    currentTab = currentScreen,
                    onTabPressed = { screen: AppScreen ->
                        viewModel.updateCurrentScreen(currentScreen = screen)
                        navController.navigate(route = screen.name)
                    },
                    navigationItemContentList = navigationItemContentList
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        val uiState by viewModel.uiState.collectAsState()

        val modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.padding_small),
                top = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small)
            )
            .fillMaxSize()

        val animationTimeMillis = 500
        NavHost(
            navController = navController,
            startDestination = AppScreen.START.name,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                if(!viewModel.isPlaying()) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(animationTimeMillis)
                    )
                }
                else {
                    fadeIn(animationSpec = tween(animationTimeMillis))
                }
            },
            exitTransition = {
                if(!viewModel.isPlaying()) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(animationTimeMillis)
                    )
                }
                else {
                    fadeOut(animationSpec = tween(animationTimeMillis))
                }
            },
            popEnterTransition = {
                if (viewModel.isPlaying()) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(animationTimeMillis)
                    )
                } else {
                    fadeIn(animationSpec = tween(animationTimeMillis))
                }
            },
            popExitTransition = {
                if(viewModel.isPlaying()) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(animationTimeMillis)
                    )
                }
                else {
                    fadeOut(animationSpec = tween(animationTimeMillis))
                }
            }
        ) {
            composable(route = AppScreen.START.name) {
                GameListScreen(
                    pointsTotalCount = uiState.currentScores,
                    onGameClicked = {
                        viewModel.pickGame(it)
                        navController.navigate(route = viewModel.getRouteByGameId(it))
                    },
                    games = uiState.games,
                    modifier = modifier
                )
            }

            composable(route = AppScreen.STATISTICS.name) {
                StatisticsScreen(
                    games = uiState.games,
                    modifier = modifier
                )
            }

            composable(route = AppScreen.SHOP.name) {
                ShopScreen(
                    currentPoints = uiState.currentScores.toLong(),
                    onBought = { cost ->
                        viewModel.removePoints(cost)
                        profileViewModel.updateItems(
                            shopViewModel.getBoughtItems()
                        )
                    },
                    viewModel = shopViewModel,
                    modifier = modifier
                )
            }

            composable(route = AppScreen.PROFILE.name) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    modifier = modifier
                )
            }

            // Games
            composable(route = AppScreen.GAME_SAPPER.name) {
                BackHandler {
                    viewModel.endGame(isSuccess = false, reward = 0)
                    navController.navigateUp()
                }

                SapperGameScreen(
                    onGameEnd = { isSuccess, reward ->
                        viewModel.endGame(isSuccess = isSuccess, reward = reward)
                        navController.navigateUp()
                    },
                    award = viewModel.getCurrentGameWinScore(),
                    modifier = modifier
                )
            }

            composable(route = AppScreen.GAME_CARD_GUESSER.name) {
                BackHandler {
                    viewModel.endGame(isSuccess = false, reward = 0)
                    navController.navigateUp()
                }

                CardGuesserGameScreen(
                    onGameEnd = { isSuccess, reward ->
                        viewModel.endGame(isSuccess = isSuccess, reward = reward)
                        navController.navigateUp()
                    },
                    award = viewModel.getCurrentGameWinScore(),
                    modifier = modifier
                )
            }

            composable(route = AppScreen.GAME_TOWER.name) {
                BackHandler {
                    viewModel.endGame(isSuccess = false, reward = 0)
                    navController.navigateUp()
                }

                GameTowerScreen(
                    onGameEnd = { isSuccess, reward ->
                        viewModel.endGame(isSuccess = isSuccess, reward = reward)
                        navController.navigateUp()
                    },
                    award = viewModel.getCurrentGameWinScore(),
                    modifier = modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesTopAppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
private fun BottomNavigationBar(
    currentTab: AppScreen,
    onTabPressed: ((AppScreen) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.screen,
                label = {
                   Text(text = navItem.text)
                },
                alwaysShowLabel = false,
                onClick = { onTabPressed(navItem.screen) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}


@Preview
@Composable
fun AppPreview() {
    SimpleGamesTheme {
        GamesApp()
    }
}

@Preview
@Composable
fun AppDarkThemePreview() {
    SimpleGamesTheme(darkTheme = true) {
        GamesApp()
    }
}