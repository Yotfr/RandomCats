package com.yotfr.randomcats.presentation.navigation

import com.yotfr.randomcats.R

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Favorite : BottomNavItem("Favorite", R.drawable.ic_favorite_outlined, "favorite")
    object Home : BottomNavItem("Home", R.drawable.ic_home, "home")
    object Profile : BottomNavItem("Profile", R.drawable.ic_profile, "profile")
}
