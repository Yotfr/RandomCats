package com.yotfr.randomcats.presentation.screens.settingstheme.event

import com.yotfr.randomcats.domain.model.Theme

sealed interface SettingsThemeEvent{
    data class ThemeChanged(val theme:Theme):SettingsThemeEvent
}