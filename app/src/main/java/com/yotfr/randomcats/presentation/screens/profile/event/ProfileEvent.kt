package com.yotfr.randomcats.presentation.screens.profile.event

sealed interface ProfileEvent{
    object SignOut: ProfileEvent
}