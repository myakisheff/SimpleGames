package com.example.simplegames.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplegames.ui.profileScreens.ProfileMainScreen
import com.example.simplegames.ui.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = ProfileScreenDest.MAIN.name,
        modifier = modifier
    ) {
        composable(route = ProfileScreenDest.MAIN.name) {
            ProfileMainScreen(
                profileImage = uiState.currentProfileImage,
                availableItems = uiState.availableItems,
                onFeatureClick = {},
                onNewImageLoad = {
                    viewModel.updateImageUri(it)
                }
            )
        }

        composable(route = ProfileScreenDest.LANGUAGES.name) {

        }
    }
}
