package com.yotfr.randomcats.presentation.screens.settingstheme.model

import com.yotfr.randomcats.domain.model.Theme

data class SettingsThemeState(
    val currentTheme: Theme = Theme.SYSTEM_DEFAULT
)
