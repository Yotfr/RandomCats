package com.yotfr.randomcats.presentation.screens.signin.event

sealed interface SignInEvent {
    object SignInClicked : SignInEvent
    data class UpdateEmailText(val newText: String) : SignInEvent
    data class UpdatePasswordText(val newText: String) : SignInEvent
}
