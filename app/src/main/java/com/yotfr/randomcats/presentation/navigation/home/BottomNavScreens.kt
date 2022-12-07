package com.yotfr.randomcats.presentation.navigation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreens(var title: String, var icon: ImageVector, var screen_route: String) {
    object Favorite : BottomNavScreens("Favorite", Icons.Filled.Favorite, "favorite")
    object Home : BottomNavScreens("Home", Icons.Filled.Home, "home")
}
