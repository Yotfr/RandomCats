package com.yotfr.randomcats.presentation.screens.settings.event

sealed interface SettingsScreenEvent{
    object NavigateToAuth:SettingsScreenEvent
    object NavigateToThemeScreen:SettingsScreenEvent
}