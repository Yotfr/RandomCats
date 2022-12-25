package com.yotfr.randomcats.presentation.screens.settings.event

sealed interface SettingsEvent {
    object SignOutPressed : SettingsEvent
    object ThemePressed : SettingsEvent
}