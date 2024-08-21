package com.example.simplegames.ui.utils

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.simplegames.ui.AppScreen

data class NavigationItemContent(
    val screen: AppScreen,
    val icon: ImageVector,
    val text: String
)