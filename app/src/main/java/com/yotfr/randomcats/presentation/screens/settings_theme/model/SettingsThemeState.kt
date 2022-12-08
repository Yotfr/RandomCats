package com.yotfr.randomcats.presentation.screens.settings_theme.model

import com.yotfr.randomcats.domain.model.Theme

data class SettingsThemeState(
    val currentTheme:Theme = Theme.SYSTEM_DEFAULT
)