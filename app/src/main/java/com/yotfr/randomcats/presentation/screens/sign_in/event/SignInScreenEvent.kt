package com.yotfr.randomcats.presentation.screens.sign_in.event

sealed interface SignInScreenEvent{
    object NavigateHome:SignInScreenEvent
    object ShowInvalidCredentialsError:SignInScreenEvent
}