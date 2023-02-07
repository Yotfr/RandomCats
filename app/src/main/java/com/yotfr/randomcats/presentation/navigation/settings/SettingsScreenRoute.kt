package com.yotfr.randomcats.presentation.navigation.settings

sealed class SettingsScreenRoute(val screen_route: String) {
    object SettingsRoute : SettingsScreenRoute("settings")
    object SettingsThemeRoute : SettingsScreenRoute("settings_theme")
}
