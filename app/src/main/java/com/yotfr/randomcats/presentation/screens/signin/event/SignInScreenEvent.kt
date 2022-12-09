package com.yotfr.randomcats.presentation.screens.signin.event

sealed interface SignInScreenEvent{
    object NavigateHome:SignInScreenEvent
    object ShowInvalidCredentialsError:SignInScreenEvent
}