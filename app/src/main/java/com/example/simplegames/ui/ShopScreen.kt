package com.example.simplegames.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplegames.R
import com.example.simplegames.ui.shopScreens.ShopItemsScreen
import com.example.simplegames.ui.shopScreens.ShopCategoriesScreen
import com.example.simplegames.ui.theme.SimpleGamesTheme
import com.example.simplegames.ui.viewModel.ShopViewModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel,
    currentPoints: Long,
    onBought: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()

    val uiState = viewModel.uiState.collectAsState()

    viewModel.setPoints(currentPoints)

    NavHost(
        navController = navController,
        startDestination = ShopScreenDest.CATEGORIES.name,
    ) {
        composable(route = ShopScreenDest.CATEGORIES.name) {
            ShopCategoriesScreen(
                onCategoryClick = {
                    viewModel.selectCategory(it)
                    navController.navigate(viewModel.getRouteByCategory())
                },
                modifier = modifier
            )
        }

        composable(route = ShopScreenDest.LANGUAGES.name) {
            ShopItemsScreen(
                currentPoints = uiState.value.currentPoints,
                shopItems = viewModel.getShopItemsByCategory(),
                onItemBought = {
                    onBought(it.price)
                    viewModel.setItemToBought(it)
                },
                modifier = modifier
            )
        }
    }
}

@Composable
fun CategoryTile(
    onCategoryClick: () -> Unit,
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCategoryClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
fun ShopScreenPreview() {
    SimpleGamesTheme {
        ShopScreen(
            currentPoints = 100L,
            viewModel = viewModel(),
            onBought = {}
        )
    }
}