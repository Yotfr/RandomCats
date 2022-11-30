package com.yotfr.randomcats.presentation.screens.splash.event

sealed interface SplashScreenEvent {
    object NavigateHome:SplashScreenEvent
    object NavigateAuth:SplashScreenEvent
}