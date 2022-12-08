package com.yotfr.randomcats.presentation.navigation.home.settings

sealed class SettingsScreenRoute(val screen_route: String) {
    object SettingsRoute:SettingsScreenRoute("settings")
}